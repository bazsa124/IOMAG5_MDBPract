using MongoDB.Bson;
using MongoDB.Bson.Serialization.Attributes;

public class Szakacs
{
    [BsonId]
    public ObjectId Id { get; set; }

    public required string nev { get; set; }

    public required int eletkor {get; set; }
    public required int fizetes {get; set; }

    public required string reszleg { get; set; }

    public required List<string> vegzettsegek { get; set; }

    public required string etterem_nev { get; set; }
}