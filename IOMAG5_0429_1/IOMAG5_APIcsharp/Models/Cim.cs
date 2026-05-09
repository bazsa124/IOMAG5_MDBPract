using MongoDB.Bson.Serialization.Attributes;

namespace MongoTest.Models
{
    public class Cim
    {
        public required string varos { get; set; }
        public required string utca { get; set; }
        public int hazszam { get; set; }
    }
}