using MongoDB.Bson;
using MongoDB.Bson.Serialization.Attributes;

namespace MongoTest.Models
{
    [BsonIgnoreExtraElements]
    public class Konyv
    {
        [BsonId]
        public ObjectId Id { get; set; }
        public required string cim { get; set; }
        public required string szerzo { get; set; }
        public required string kiado { get; set; }
        public required int kiadas_eve { get; set; }
        public required string nyelv { get; set; }
        public required string[] mufajok { get; set; }
    }
}
