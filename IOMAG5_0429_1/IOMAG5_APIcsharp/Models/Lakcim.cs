using MongoDB.Bson.Serialization.Attributes;

namespace MongoTest.Models
{
    public class Lakcim
    {
        public required string iranyitoszam { get; set; }
        public required string utca { get; set; }
        public required string hazszam { get; set; }
    }
}
