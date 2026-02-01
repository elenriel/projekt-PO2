using System;
using System.Collections.Generic;
using Microsoft.Data.SqlClient;
using TRS_APP.DAL;
using TRS_APP.Models;

namespace TRS_APP.Repositories;

public class ClientRepository
{
    public List<Client> GetAll()
    {
        var list = new List<Client>();

        using var con = new SqlConnection(Db.ConnectionString);
        con.Open();

        using var cmd = new SqlCommand("SELECT ClientId, FirstName, LastName, Email FROM Clients ORDER BY ClientId", con);
        using var r = cmd.ExecuteReader();

        while (r.Read())
        {
            list.Add(new Client
            {
                ClientId = Convert.ToInt32(r["ClientId"]),
                FirstName = Convert.ToString(r["FirstName"]) ?? "",
                LastName = Convert.ToString(r["LastName"]) ?? "",
                Email = r["Email"] == DBNull.Value ? null : Convert.ToString(r["Email"])
            });
        }

        return list;
    }

    public void Add(string firstName, string lastName, string? email)
    {
        using var con = new SqlConnection(Db.ConnectionString);
        con.Open();

        using var cmd = new SqlCommand(
            "INSERT INTO Clients(FirstName, LastName, Email) VALUES (@f, @l, @e)", con);

        cmd.Parameters.AddWithValue("@f", firstName);
        cmd.Parameters.AddWithValue("@l", lastName);
        cmd.Parameters.AddWithValue("@e", (object?)email ?? DBNull.Value);

        cmd.ExecuteNonQuery();
    }

    public void Delete(int clientId)
    {
        using var con = new SqlConnection(Db.ConnectionString);
        con.Open();

        using var cmd = new SqlCommand("DELETE FROM Clients WHERE ClientId=@id", con);
        cmd.Parameters.AddWithValue("@id", clientId);
        cmd.ExecuteNonQuery();
    }
}

