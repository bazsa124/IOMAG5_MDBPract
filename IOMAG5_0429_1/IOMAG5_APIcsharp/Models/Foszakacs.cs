using MongoDB.Bson;
using MongoDB.Bson.Serialization.Attributes;

namespace MongoTest.Models
{
    public class Foszakacs
    {
        [BsonId]
        public ObjectId Id { get; set; }
        public required string nev { get; set; }
        public int eletkor { get; set; }
        public required List<string> vegzettseg { get; set; }
        public required string _fkod { get; set; }
        public required string _e_f { get; set; }
    }
}