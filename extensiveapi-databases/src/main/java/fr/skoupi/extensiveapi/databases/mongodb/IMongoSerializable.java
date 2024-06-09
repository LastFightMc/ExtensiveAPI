package fr.skoupi.extensiveapi.databases.mongodb;

/*  IMongoSerializable
 * By: vSKAH <vskahhh@gmail.com>
 * Created with IntelliJ IDEA
 * For the project ExtensiveAPI
 */

import org.bson.Document;

public interface IMongoSerializable<R> {

    R fromMongoDocument(Document document);

    Document toMongoDocument();


}
