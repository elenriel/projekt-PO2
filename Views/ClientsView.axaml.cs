using System;
using System.Threading.Tasks;
using Avalonia.Controls;
using Avalonia.Threading;
using TRS_APP.Models;
using TRS_APP.Repositories;

namespace TRS_APP.Views;

public partial class ClientsView : UserControl
{
    private readonly ClientRepository _repo = new();

    private DataGrid _clientsGrid => this.FindControl<DataGrid>("ClientsGrid");
    private TextBox _firstNameBox => this.FindControl<TextBox>("FirstNameBox");
    private TextBox _lastNameBox  => this.FindControl<TextBox>("LastNameBox");
    private TextBox _emailBox     => this.FindControl<TextBox>("EmailBox");
    private TextBlock _statusTxt  => this.FindControl<TextBlock>("StatusTxt");

    public ClientsView()
    {
        InitializeComponent();

        this.FindControl<Button>("RefreshBtn").Click += async (_, __) => await LoadAsync();
        this.FindControl<Button>("AddBtn").Click     += async (_, __) => await AddAsync();
        this.FindControl<Button>("DeleteBtn").Click  += async (_, __) => await DeleteAsync();

        _ = LoadAsync();
    }

    private async Task AddAsync()
    {
        try
        {
            var first = _firstNameBox.Text?.Trim();
            var last  = _lastNameBox.Text?.Trim();
            var email = string.IsNullOrWhiteSpace(_emailBox.Text) ? null : _emailBox.Text.Trim();

            if (string.IsNullOrWhiteSpace(first) || string.IsNullOrWhiteSpace(last))
            {
                _statusTxt.Text = "Podaj imię i nazwisko.";
                return;
            }

            _repo.Add(first!, last!, email);

            _firstNameBox.Text = "";
            _lastNameBox.Text = "";
            _emailBox.Text = "";

            await LoadAsync();
            _statusTxt.Text = "OK: dodano klienta.";
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
            if (_clientsGrid.SelectedItem is Client c)
            {
                _repo.Delete(c.ClientId);
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
                _clientsGrid.ItemsSource = null;
                _clientsGrid.ItemsSource = data;
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
