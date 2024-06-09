package fr.skoupi.extensiveapi.databases.mongodb;

/*  MongoCredentials
 * By: vSKAH <vskahhh@gmail.com>
 * Created with IntelliJ IDEA
 * For the project FirstHomes
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MongoCredentials {

    @JsonProperty("mongo_url")
    public String mongoUrl;

}
