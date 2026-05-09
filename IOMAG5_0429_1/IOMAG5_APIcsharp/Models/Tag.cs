using MongoDB.Bson;
using MongoDB.Bson.Serialization.Attributes;

namespace MongoTest.Models
{
    [BsonIgnoreExtraElements]
    public class Tag
    {
        [BsonId]
        public ObjectId Id { get; set; }

        public required string nev { get; set; }

        public required Lakcim lakcim { get; set; }

        public required string email { get; set; }

        public required string telefonszam { get; set; }

        public required int tagdij { get; set; }

        public required int kor { get; set; }

        public required string kedvenc_kiado { get; set; }

        public required List<string> erdeklodesek { get; set; }
    }
}
