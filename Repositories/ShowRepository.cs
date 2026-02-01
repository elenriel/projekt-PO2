using System;
using System.Collections.Generic;
using Microsoft.Data.SqlClient;
using TRS_APP.DAL;
using TRS_APP.Models;

namespace TRS_APP.Repositories;

public class ShowRepository
{
    public List<Show> GetAll()
    {
        var list = new List<Show>();

        using var con = new SqlConnection(Db.ConnectionString);
        con.Open();

        using var cmd = new SqlCommand(
            "SELECT ShowId, EventId, StartTime, Price FROM Shows ORDER BY ShowId", con);

        using var r = cmd.ExecuteReader();
        while (r.Read())
        {
            list.Add(new Show
            {
                ShowId = Convert.ToInt32(r["ShowId"]),
                EventId = Convert.ToInt32(r["EventId"]),
                StartTime = Convert.ToDateTime(r["StartTime"]),
                Price = Convert.ToDecimal(r["Price"])
            });
        }

        return list;
    }

    public void AddSample()
{
    using var con = new SqlConnection(Db.ConnectionString);
    con.Open();

    // weź dowolne istniejące EventId
    using var getEvent = new SqlCommand("SELECT TOP 1 EventId FROM Events ORDER BY EventId", con);
    var eventIdObj = getEvent.ExecuteScalar();
    if (eventIdObj == null)
        throw new Exception("Brak wydarzeń w tabeli Events. Dodaj najpierw wydarzenie.");

    var eventId = Convert.ToInt32(eventIdObj);

    using var cmd = new SqlCommand(
        "INSERT INTO Shows(EventId, StartTime, Price) VALUES (@eid, DATEADD(day, 1, SYSUTCDATETIME()), 99.99)", con);

    cmd.Parameters.AddWithValue("@eid", eventId);
    cmd.ExecuteNonQuery();
}

    public void Delete(int showId)
    {
        using var con = new SqlConnection(Db.ConnectionString);
        con.Open();

        using var cmd = new SqlCommand("DELETE FROM Shows WHERE ShowId=@id", con);
        cmd.Parameters.AddWithValue("@id", showId);
        cmd.ExecuteNonQuery();
    }
}
