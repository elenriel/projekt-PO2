using System;
using System.Data;
using System.Threading.Tasks;
using Avalonia.Controls;
using Avalonia.Threading;
using TRS_APP.DAL;

namespace TRS_APP.Views;

public partial class SqlConsoleView : UserControl
{
    public SqlConsoleView()
    {
        InitializeComponent();

        RunBtn.Click += async (_, __) => await RunAsync();
    }

    private Task RunAsync()
    {
        try
        {
            var sql = SqlBox.Text?.Trim() ?? "";
            if (string.IsNullOrWhiteSpace(sql))
            {
                StatusTxt.Text = "Wpisz zapytanie SQL.";
                return Task.CompletedTask;
            }

            if (sql.StartsWith("SELECT", StringComparison.OrdinalIgnoreCase) ||
                sql.StartsWith("WITH", StringComparison.OrdinalIgnoreCase))
            {
                var dt = Db.Query(sql);

                Dispatcher.UIThread.Post(() =>
                {
                    ResultGrid.ItemsSource = null;
                    ResultGrid.ItemsSource = dt.DefaultView;
                    StatusTxt.Text = $"OK: wierszy {dt.Rows.Count}";
                });
            }
            else
            {
                var affected = Db.Exec(sql);

                Dispatcher.UIThread.Post(() =>
                {
                    ResultGrid.ItemsSource = null;
                    StatusTxt.Text = $"OK: zmieniono {affected} wierszy.";
                });
            }
        }
        catch (Exception ex)
        {
            Dispatcher.UIThread.Post(() => StatusTxt.Text = "BŁĄD: " + ex.Message);
            Console.WriteLine(ex);
        }

        return Task.CompletedTask;
    }
}
