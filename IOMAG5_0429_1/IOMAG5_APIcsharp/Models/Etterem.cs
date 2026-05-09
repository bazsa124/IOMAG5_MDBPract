using MongoDB.Bson;
using MongoDB.Bson.Serialization.Attributes;

namespace MongoTest.Models
{
    [BsonIgnoreExtraElements]
    public class Etterem
    {
        [BsonId]
        public ObjectId Id { get; set; }
        public required string nev { get; set; }
        public required Cim cim { get; set; }
        public required int csillag { get; set; }
        public required string[] specialitasok { get; set; }
    }
}