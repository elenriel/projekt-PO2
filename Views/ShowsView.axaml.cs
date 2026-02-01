using System;
using System.Threading.Tasks;
using Avalonia.Controls;
using Avalonia.Threading;
using TRS_APP.Models;
using TRS_APP.Repositories;

namespace TRS_APP.Views;

public partial class ShowsView : UserControl
{
    private readonly ShowRepository _repo = new();

    private DataGrid _showsGrid => this.FindControl<DataGrid>("ShowsGrid");
    private TextBlock _statusTxt => this.FindControl<TextBlock>("StatusTxt");

    public ShowsView()
    {
        InitializeComponent();

        this.FindControl<Button>("RefreshBtn").Click += async (_, __) => await LoadAsync();

        this.FindControl<Button>("AddBtn").Click += async (_, __) =>
        {
            try
            {
                _statusTxt.Text = "Dodaję seans...";
                _repo.AddSample();
                await LoadAsync();
                _statusTxt.Text = "OK: dodano seans.";
            }
            catch (Exception ex)
            {
                _statusTxt.Text = "BŁĄD: " + ex.Message;
                Console.WriteLine(ex);
            }
        };

        this.FindControl<Button>("DeleteBtn").Click += async (_, __) =>
        {
            try
            {
                if (_showsGrid.SelectedItem is Show s)
                {
                    _statusTxt.Text = "Usuwam...";
                    _repo.Delete(s.ShowId);
                    await LoadAsync();
                    _statusTxt.Text = "OK: usunięto.";
                }
            }
            catch (Exception ex)
            {
                _statusTxt.Text = "BŁĄD: " + ex.Message;
                Console.WriteLine(ex);
            }
        };

        _ = LoadAsync();
    }

    private Task LoadAsync()
    {
        try
        {
            var data = _repo.GetAll();

            Dispatcher.UIThread.Post(() =>
            {
                _showsGrid.ItemsSource = null;
                _showsGrid.ItemsSource = data;
                _statusTxt.Text = $"Wczytano: {data.Count}";
            });
        }
        catch (Exception ex)
        {
            Dispatcher.UIThread.Post(() => _statusTxt.Text = "BŁĄD: " + ex.Message);
            Console.WriteLine(ex);
        }

        return Task.CompletedTask;
    }
}
