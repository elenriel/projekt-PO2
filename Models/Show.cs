using System;

namespace TRS_APP.Models;

public class Show
{
    public int ShowId { get; set; }
    public int EventId { get; set; }
    public DateTime StartTime { get; set; }
    public decimal Price { get; set; }
}
