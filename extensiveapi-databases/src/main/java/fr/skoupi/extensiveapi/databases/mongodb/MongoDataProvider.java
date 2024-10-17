package fr.skoupi.extensiveapi.databases.mongodb;

/*  MongoDataProvider
 *  By: vSKAH <vskahhh@gmail.com>

 * Created with IntelliJ IDEA
 * For the project ExtensiveAPI
 */

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.InsertManyOptions;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.InsertManyResult;
import lombok.Getter;
import lombok.Setter;
import org.bson.BsonValue;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.conversions.Bson;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.function.BiConsumer;

@Getter
public class MongoDataProvider {

    // It's just a variable declaration.
    private final String databaseName;
    private final String collectionName;
    @Setter
    private MongoCollection<Document> mongoCollection;

    // It's a constructor.
    public MongoDataProvider(MongoDataSource dataSource, String databaseName, String collectionName) {
        this(dataSource, databaseName, collectionName, null);
    }

    //It's another constructor, but with codec registry.
    //Codec registry is used to serialize and deserialize custom plain old java objects.
    public MongoDataProvider(MongoDataSource dataSource, String databaseName, String collectionName, CodecRegistry codecRegistry) {
        this.databaseName = databaseName;
        this.collectionName = collectionName;
        if (codecRegistry != null)
            this.mongoCollection = dataSource.getMongoClient().getDatabase(databaseName).getCollection(collectionName).withCodecRegistry(codecRegistry);
        else
            this.mongoCollection = dataSource.getMongoClient().getDatabase(databaseName).getCollection(collectionName);
    }

    /**
     * Get the first document from the collection that matches the filter.
     *
     * @param filter The filter to use to find the document.
     * @return A single document that matches the filter.
     */
    public Document syncGetDocumentFromFilter(Bson filter) {
        return mongoCollection.find(filter).first();
    }


    public Document syncGetDocumentFromFilter(Bson filter, Bson projections) {
        return mongoCollection.find(filter).projection(projections).first();
    }


    /**
     * "This function takes a filter, an executor service, and a completable future, and then submits a task to the executor
     * service that completes the future with the result of the synchronous get document from filter function."
     * The first thing to notice is that the function is asynchronous. It takes an executor service and a completable future
     * as parameters. The executor service is used to submit a task that completes the future
     *
     * @param filter          The filter to use to find the document.
     * @param executorService The executor service to use for the asynchronous operation.
     * @param documentFuture  This is the future object that will be completed with the document.
     */
    public void asyncGetDocumentFromUniqueId(Bson filter, ExecutorService executorService, CompletableFuture<Document> documentFuture) {
        executorService.submit(() -> {
            try {
                documentFuture.complete(syncGetDocumentFromFilter(filter));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void asyncGetDocumentFromUniqueId(Bson filter, Bson projection, ExecutorService executorService, CompletableFuture<Document> documentFuture) {
        executorService.submit(() -> {
            try {
                documentFuture.complete(syncGetDocumentFromFilter(filter, projection));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }


    /**
     * Insert a document into the collection and return true if the operation was acknowledged.
     *
     * @param document The document to insert.
     * @return A boolean value.
     */
    public boolean syncInsertDocument(Document document) {
        return mongoCollection.insertOne(document).wasAcknowledged();
    }

    /**
     * "Insert a document into the database asynchronously, using the given executor service, and complete the given future
     * with the result."
     * The function is a bit more complicated than that, but not much. It takes a document, an executor service, and a future.
     * It then submits a task to the executor service that completes the future with the result of the synchronous insert
     * function
     *
     * @param document        The document to insert.
     * @param executorService The executor service to use for the asynchronous operation.
     * @param future          The future object that will be completed when the operation is done.
     */
    public void asyncInsertDocument(Document document, ExecutorService executorService, CompletableFuture<Boolean> future) {
        executorService.submit(() -> {
            try {
                future.complete(syncInsertDocument(document));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }


    /**
     * "Inserts the given documents into the collection, and returns a map of the inserted document IDs."
     * The first parameter is a list of documents to insert. The second parameter is a boolean that indicates whether the
     * insert should be ordered or unordered. The third parameter is a callback function that will be called when the insert
     * is complete
     *
     * @param documents The documents to insert.
     * @param ordered   If true, when an insert fails, return without performing the remaining writes. If false, when a write
     *                  fails, continue with the remaining writes, if any. The default is true.
     * @param consumer  A BiConsumer that takes a boolean and a Map<Integer, BsonValue> as parameters. The boolean is whether
     *                  the operation was acknowledged, and the Map<Integer, BsonValue> is the inserted IDs.
     */
    public void syncInsertMultipleDocuments(List<Document> documents, boolean ordered, BiConsumer<Boolean, Map<Integer, BsonValue>> consumer) {
        InsertManyResult result = this.mongoCollection.insertMany(documents, (new InsertManyOptions()).ordered(ordered));
        try {
            consumer.accept(result.wasAcknowledged(), result.getInsertedIds());
        } catch (Exception e) {
            System.err.println("Une erreur s'est produite lors de l'insertion de plusieurs documents : " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * "Insert multiple documents into the collection, and when done, call the consumer with the results."
     * The first parameter is a list of documents to insert. The second parameter is a boolean that indicates whether the
     * insert should be ordered or unordered. The third parameter is a consumer that will be called with the results of the
     * insert. The fourth parameter is an executor service that will be used to execute the insert
     *
     * @param documents       The documents to insert.
     * @param ordered         If true, the server will stop processing the batch after the first error. If false, the server will
     *                        continue processing the batch even if there are errors.
     * @param consumer        A BiConsumer that takes a boolean and a map of integers to BsonValues. The boolean is true if the
     *                        operation was successful, false otherwise. The map is a map of the index of the document in the list to the BsonValue
     *                        of the document's _id.
     * @param executorService The executor service to use for the asynchronous operation.
     */
    public void asyncInsertMultipleDocuments(List<Document> documents, boolean ordered, BiConsumer<Boolean, Map<Integer, BsonValue>> consumer, ExecutorService executorService) {
        executorService.submit(() -> {
            try {
                syncInsertMultipleDocuments(documents, ordered, consumer);
            } catch (Exception e) {
                System.err.println("Une erreur s'est produite lors de l'insertion de plusieurs documents : " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    /**
     * > Replace the first document that matches the filter with the given document
     *
     * @param filter   The filter to find the document to replace.
     * @param document The document to be inserted.
     * @return A boolean value.
     */
    public boolean syncReplaceDocument(Bson filter, Document document) {
        return mongoCollection.replaceOne(filter, document).wasAcknowledged();
    }

    /**
     * "Replace a document in the database, asynchronously."
     * The first parameter is the filter, which is a Bson object. The second parameter is the document to replace the old
     * document with. The third parameter is the ExecutorService object, which is used to execute the task asynchronously. The
     * fourth parameter is the CompletableFuture object, which is used to return the result of the task
     *
     * @param filter          The filter to find the document to replace.
     * @param document        The document to replace the existing document with.
     * @param executorService The executor service to use for the asynchronous operation.
     * @param future          The future object that will be completed when the operation is done.
     */
    public void asyncReplaceDocument(Bson filter, Document document, ExecutorService executorService, CompletableFuture<Boolean> future) {
        executorService.submit(() -> {
            try {
                future.complete(syncReplaceDocument(filter, document));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * > This function updates one document in the collection, using the filter to find the document to update
     *
     * @param filter   The filter to apply to the query.
     * @param document The document to be inserted.
     * @param upsert   If true, creates a new document when no document matches the query criteria. The default is false, which
     *                 does not insert a new document when no match is found.
     * @return A boolean value.
     */
    public boolean syncUpdateOneDocument(Bson filter, Bson document, boolean upsert) {
        return mongoCollection.updateOne(filter, document, new UpdateOptions().upsert(upsert)).wasAcknowledged();
    }

    /**
     * "This function updates one document in the database, and returns a boolean value indicating whether the update was
     * successful."
     * The first parameter is a filter, which is a Bson object that specifies which document to update. The second parameter
     * is a Document object that contains the new values for the document. The third parameter is a boolean value that
     * indicates whether the update should insert a new document if the filter doesn't match any documents. The fourth
     * parameter is an ExecutorService object that is used to execute the update operation in a separate thread. The fifth
     * parameter is a CompletableFuture object that is used to return the result of the update operation
     *
     * @param filter          The filter to find the document to update.
     * @param document        The document to update.
     * @param upsert          If true, the update will insert a new document if no document matches the filter.
     * @param executorService The executor service to use for the asynchronous operation.
     * @param future          The future object that will be completed when the update is done.
     */
    public void asyncUpdateOneDocument(Bson filter, Bson document, boolean upsert, ExecutorService executorService, CompletableFuture<Boolean> future) {
        executorService.submit(() -> {
            try {
                future.complete(syncUpdateOneDocument(filter, document, upsert));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * > This function updates many documents in the collection, using the filter to find the documents to update, and the
     * documents to update with
     *
     * @param filter    The filter to apply to the query.
     * @param documents The documents to update.
     * @param upsert    If true, creates a new document when no document matches the query criteria. The default value is false,
     *                  which does not insert a new document when no match is found.
     * @return A boolean value.
     */
    public boolean syncUpdateManyDocuments(Bson filter, List<Document> documents, boolean upsert) {
        return mongoCollection.updateMany(filter, documents, new UpdateOptions().upsert(upsert)).wasAcknowledged();
    }

    /**
     * "This function updates many documents in the database, and returns a boolean value indicating whether or not the
     * operation was successful."
     * The first parameter is a filter, which is used to select the documents to update. The second parameter is a list of
     * documents, which are used to update the selected documents. The third parameter is a boolean value, which indicates
     * whether or not the operation should insert a new document if no documents match the filter. The fourth parameter is an
     * executor service, which is used to execute the operation asynchronously. The fifth parameter is a future, which is used
     * to return the result of the operation
     *
     * @param filter          The filter to apply to the documents.
     * @param documents       The documents to update.
     * @param upsert          If true, the update will insert a new document if no document matches the query.
     * @param executorService The executor service to use for the asynchronous operation.
     * @param future          The future object that will be completed when the operation is done.
     */
    public void asyncUpdateManyDocuments(Bson filter, List<Document> documents, boolean upsert, ExecutorService executorService, CompletableFuture<Boolean> future) {
        executorService.submit(() -> {
            try {
                future.complete(syncUpdateManyDocuments(filter, documents, upsert));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Delete the first document that matches the filter.
     *
     * @param filter The filter to apply to the query.
     * @return A boolean value.
     */
    public boolean syncDeleteDocument(Bson filter) {
        return mongoCollection.deleteOne(filter).wasAcknowledged();
    }

    /**
     * "Delete a document from the database, asynchronously."
     * The first parameter is the filter, which is the same as the filter used in the synchronous version of this function
     *
     * @param filter          The filter to use to find the document to delete.
     * @param executorService The executor service to use for the asynchronous operation.
     * @param future          The future object that will be completed when the operation is done.
     */
    public void asyncDeleteDocument(Bson filter, ExecutorService executorService, CompletableFuture<Boolean> future) {
        executorService.submit(() -> {
            try {
                future.complete(syncDeleteDocument(filter));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * "This function returns a cursor to all documents in the collection that match the filter."
     * The first line of the function is a comment. Comments are ignored by the compiler. They are used to explain what the
     * code does
     *
     * @param filter A Bson object that represents the filter to be applied to the query.
     * @return A MongoCursor<Document> object.
     */
    public MongoCursor<Document> syncGetAllDocuments(Bson filter) {
        if (filter == null) return mongoCollection.find().iterator();
        return mongoCollection.find(filter).iterator();
    }

    /**
     * "This function asynchronously gets all documents from the database that match the given filter, and then completes the
     * given future with the result."
     * The first parameter is the filter, which is a Bson object. The second parameter is the executor service, which is an
     * ExecutorService object. The third parameter is the future, which is a CompletableFuture object
     *
     * @param filter          The filter to use when querying the database.
     * @param executorService The executor service to use for the asynchronous operation.
     * @param future          The future object that will be completed with the result of the operation.
     */
    public void asyncGetAllDocuments(Bson filter, ExecutorService executorService, CompletableFuture<MongoCursor<Document>> future) {
        executorService.submit(() -> {
            try {
                future.complete(syncGetAllDocuments(filter));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

}
