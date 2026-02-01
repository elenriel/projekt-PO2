using System;
using System.Collections.Generic;
using Microsoft.Data.SqlClient;
using TRS_APP.DAL;
using TRS_APP.Models;

namespace TRS_APP.Repositories;

public class EventRepository
{
    public List<Event> GetAll()
    {
        var list = new List<Event>();

        using var con = new SqlConnection(Db.ConnectionString);
        con.Open();

        using var cmd = new SqlCommand(
            "SELECT EventId, Title, Location FROM Events ORDER BY EventId", con);

        using var r = cmd.ExecuteReader();
        while (r.Read())
        {
            list.Add(new Event
            {
                EventId = Convert.ToInt32(r["EventId"]),
                Title = Convert.ToString(r["Title"]) ?? "",
                Location = Convert.ToString(r["Location"]) ?? ""
            });
        }

        return list;
    }

    public void Add(string title, string location)
    {
        using var con = new SqlConnection(Db.ConnectionString);
        con.Open();

        using var cmd = new SqlCommand(
            "INSERT INTO Events(Title, Location) VALUES (@t, @l)", con);

        cmd.Parameters.AddWithValue("@t", title);
        cmd.Parameters.AddWithValue("@l", location);
        cmd.ExecuteNonQuery();
    }

    public void Delete(int eventId)
    {
        using var con = new SqlConnection(Db.ConnectionString);
        con.Open();

        using var cmd = new SqlCommand("DELETE FROM Events WHERE EventId=@id", con);
        cmd.Parameters.AddWithValue("@id", eventId);
        cmd.ExecuteNonQuery();
    }
}
