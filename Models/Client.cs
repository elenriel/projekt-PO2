namespace TRS_APP.Models;

public class Client
{
    public int ClientId { get; set; }
    public string FirstName { get; set; } = "";
    public string LastName  { get; set; } = "";
    public string? Email    { get; set; }
}
