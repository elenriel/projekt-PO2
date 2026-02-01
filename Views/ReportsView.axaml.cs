using System;
using Avalonia.Controls;
using Avalonia.Threading;
using Microsoft.Data.SqlClient;
using TRS_APP.DAL;

namespace TRS_APP.Views;

public partial class ReportsView : UserControl
{
    public ReportsView()
    {
        InitializeComponent();

        Report1Btn.Click += (_, __) => RunReport1();
        Report2Btn.Click += (_, __) => RunReport2();
        Report3Btn.Click += (_, __) => RunReport3();
    }

    private void RunReport1()
    {
        try
        {
            var dt = Db.Query(@"
                SELECT e.Title, COUNT(*) AS Reservations
                FROM Reservations r
                JOIN Shows s ON s.ShowId = r.ShowId
                JOIN Events e ON e.EventId = s.EventId
                WHERE r.Status = 'ACTIVE'
                GROUP BY e.Title
                ORDER BY Reservations DESC
            ");

            ReportGrid.ItemsSource = dt.DefaultView; // <<< NAJWAŻNIEJSZE
            StatusTxt.Text = "OK: rezerwacje per wydarzenie.";
        }
        catch (Exception ex)
        {
            StatusTxt.Text = "BŁĄD: " + ex.Message;
            Console.WriteLine(ex);
        }
    }

    private void RunReport2()
    {
        try
        {
            var dt = Db.Query(@"
                SELECT e.Title, SUM(s.Price) AS Revenue
                FROM Reservations r
                JOIN Shows s ON s.ShowId = r.ShowId
                JOIN Events e ON e.EventId = s.EventId
                WHERE r.Status = 'ACTIVE'
                GROUP BY e.Title
                ORDER BY Revenue DESC
            ");

            ReportGrid.ItemsSource = dt.DefaultView;
            StatusTxt.Text = "OK: przychód per wydarzenie.";
        }
        catch (Exception ex)
        {
            StatusTxt.Text = "BŁĄD: " + ex.Message;
            Console.WriteLine(ex);
        }
    }

    private void RunReport3()
    {
        try
        {
            var dt = Db.Query(@"
                SELECT TOP 20 ReservationId, Status, SeatNo, CreatedAt,
                               FirstName, LastName, Email, Title, Location, StartTime, Price
                FROM dbo.v_ReservationList
                ORDER BY CreatedAt DESC
            ");

            ReportGrid.ItemsSource = dt.DefaultView;
            StatusTxt.Text = "OK: ostatnie rezerwacje.";
        }
        catch (Exception ex)
        {
            StatusTxt.Text = "BŁĄD: " + ex.Message;
            Console.WriteLine(ex);
        }
    }
}
