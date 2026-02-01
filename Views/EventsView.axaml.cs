using System;
using System.Threading.Tasks;
using Avalonia.Controls;
using Avalonia.Threading;
using TRS_APP.Models;
using TRS_APP.Repositories;

namespace TRS_APP.Views;

public partial class EventsView : UserControl
{
    private readonly EventRepository _repo = new();

    private DataGrid _eventsGrid => this.FindControl<DataGrid>("EventsGrid");
    private TextBox _titleBox => this.FindControl<TextBox>("TitleBox");
    private TextBox _locationBox => this.FindControl<TextBox>("LocationBox");
    private TextBlock _statusTxt => this.FindControl<TextBlock>("StatusTxt");

    public EventsView()
    {
        InitializeComponent();

        this.FindControl<Button>("RefreshBtn").Click += async (_, __) => await LoadAsync();
        this.FindControl<Button>("AddBtn").Click += async (_, __) => await AddAsync();
        this.FindControl<Button>("DeleteBtn").Click += async (_, __) => await DeleteAsync();

        _ = LoadAsync();
    }

    private async Task AddAsync()
    {
        try
        {
            var title = _titleBox.Text?.Trim();
            var loc = _locationBox.Text?.Trim();

            if (string.IsNullOrWhiteSpace(title) || string.IsNullOrWhiteSpace(loc))
            {
                _statusTxt.Text = "Podaj tytuł i lokalizację.";
                return;
            }

            _repo.Add(title!, loc!);

            _titleBox.Text = "";
            _locationBox.Text = "";

            await LoadAsync();
            _statusTxt.Text = "OK: dodano wydarzenie.";
        }
        catch (Exception ex)
        {
            _statusTxt.Text = "BŁĄD: " + ex.Message;
            Console.WriteLine(ex);
        }
    }

    private async Task DeleteAsync()
    {
        try
        {
            if (_eventsGrid.SelectedItem is Event e)
            {
                _repo.Delete(e.EventId);
                await LoadAsync();
                _statusTxt.Text = "OK: usunięto.";
            }
        }
        catch (Exception ex)
        {
            _statusTxt.Text = "BŁĄD: " + ex.Message;
            Console.WriteLine(ex);
        }
    }

    private Task LoadAsync()
    {
        try
        {
            var data = _repo.GetAll();

            Dispatcher.UIThread.Post(() =>
            {
                _eventsGrid.ItemsSource = null;
                _eventsGrid.ItemsSource = data;
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
