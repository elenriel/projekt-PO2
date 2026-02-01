using System.Data;
using Microsoft.Data.SqlClient;

namespace TRS_APP.DAL;

public static class Db
{
    public static string ConnectionString =
        "Server=localhost,1433;Database=TicketReservation;User Id=sa;Password=Strong!Pass123;Encrypt=False;TrustServerCertificate=True;";

    public static SqlConnection Open()
    {
        var conn = new SqlConnection(ConnectionString);
        conn.Open();
        return conn;
    }

    public static DataTable Query(string sql, params SqlParameter[] parameters)
    {
        using var conn = Open();
        using var cmd = new SqlCommand(sql, conn);
        if (parameters is { Length: > 0 }) cmd.Parameters.AddRange(parameters);

        using var da = new SqlDataAdapter(cmd);
        var dt = new DataTable();
        da.Fill(dt);
        return dt;
    }

    public static int Exec(string sql, params SqlParameter[] parameters)
    {
        using var conn = Open();
        using var cmd = new SqlCommand(sql, conn);
        if (parameters is { Length: > 0 }) cmd.Parameters.AddRange(parameters);
        return cmd.ExecuteNonQuery();
    }

    public static object? Scalar(string sql, params SqlParameter[] parameters)
    {
        using var conn = Open();
        using var cmd = new SqlCommand(sql, conn);
        if (parameters is { Length: > 0 }) cmd.Parameters.AddRange(parameters);
        return cmd.ExecuteScalar();
    }
}
