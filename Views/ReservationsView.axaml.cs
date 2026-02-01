using System;
using System.Threading.Tasks;
using Avalonia.Controls;
using Avalonia.Threading;
using Microsoft.Data.SqlClient;
using TRS_APP.DAL;

namespace TRS_APP.Views;

public partial class ReservationsView : UserControl
{
    public ReservationsView()
    {
        InitializeComponent();

        RefreshBtn.Click += async (_, __) => await LoadAsync();

        CreateBtn.Click += async (_, __) =>
        {
            try
            {
                if (!int.TryParse(ClientIdBox.Text, out var clientId) ||
                    !int.TryParse(ShowIdBox.Text, out var showId) ||
                    !int.TryParse(SeatNoBox.Text, out var seatNo))
                {
                    StatusTxt.Text = "Podaj poprawne liczby: ClientId, ShowId, SeatNo.";
                    return;
                }

                StatusTxt.Text = "Tworzę rezerwację...";

                Db.Exec(
                    "EXEC dbo.sp_CreateReservation @ClientId, @ShowId, @SeatNo",
                    new SqlParameter("@ClientId", clientId),
                    new SqlParameter("@ShowId", showId),
                    new SqlParameter("@SeatNo", seatNo)
                );

                await LoadAsync();
                StatusTxt.Text = "OK: utworzono rezerwację.";
            }
            catch (Exception ex)
            {
                StatusTxt.Text = "BŁĄD: " + ex.Message;
                Console.WriteLine(ex);
            }
        };

        _ = LoadAsync();
    }

    private Task LoadAsync()
    {
        try
        {
            var dt = Db.Query(@"
                SELECT TOP 200 *
                FROM dbo.v_ReservationList
                ORDER BY CreatedAt DESC
            ");

            Dispatcher.UIThread.Post(() =>
            {
                ResGrid.ItemsSource = dt.DefaultView;
            });
        }
        catch (Exception ex)
        {
            Dispatcher.UIThread.Post(() => StatusTxt.Text = "BŁĄD: " + ex.Message);
            Console.WriteLine(ex);
        }

        return Task.CompletedTask;
    }
}
