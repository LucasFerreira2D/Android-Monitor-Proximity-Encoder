package com.lfereira.iot;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.lfereira.iot.banco.DataBase;
import com.lfereira.iot.banco.modelo.Servidor;
import com.lfereira.iot.model.GyroData;
import com.lfereira.iot.model.GyroResponse;
import com.lfereira.iot.model.ProximityData;
import com.lfereira.iot.model.ProximityResponse;
import com.lfereira.iot.service.ApiClient;
import com.lfereira.iot.service.ApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private Servidor servidor;
    private ImageView imageViewConfiguracoes;
    private ImageView imageViewRefresh;
    private LineChart proximityChart;
    private BarChart gyroChart;
    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable runnable;
    private final int interval = 1000; // 15 segundos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        try {
            servidor = DataBase.getInstance(MainActivity.this).servidorDao().buscar().get();
        }catch (Exception e){
            e.printStackTrace();
        }


        if (servidor == null) {
            configuracoes();
        }else{
            try {
                servidor = DataBase.getInstance(MainActivity.this).servidorDao().buscar().get();
            }catch (Exception e){
                e.printStackTrace();
            }
        }



        imageViewConfiguracoes = findViewById(R.id.imageViewConfiguracoes);
        imageViewRefresh = findViewById(R.id.imageViewRefresh);
        proximityChart = findViewById(R.id.proximityChart);
        gyroChart = findViewById(R.id.gyroChart);

        if(servidor != null) {

            fetchProximityData();
            fetchGyroData();

            // Inicializar o runnable para atualizar os gráficos a cada 15 segundos
            runnable = new Runnable() {
                @Override
                public void run() {
                    // Funções para buscar os dados
                    fetchProximityData();
                    fetchGyroData();

                    // Agendar a próxima execução após 15 segundos
                    handler.postDelayed(this, interval);
                }
            };

            // Começa o ciclo de atualizações
            handler.post(runnable);

        }

        imageViewConfiguracoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                configuracoes();

            }
        });


        imageViewRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(servidor != null) {
                    fetchProximityData();
                    fetchGyroData();
                }
            }
        });

    }

    @SuppressLint("SetTextI18n")
    private void configuracoes(){

        // Dentro de uma Activity ou Fragment
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_url_porta, null);

        EditText editTextUrl = dialogView.findViewById(R.id.editTextUrl);
        EditText editTextPort = dialogView.findViewById(R.id.editTextPorta);

        if(servidor != null){
            editTextUrl.setText(servidor.getUrl());
            editTextPort.setText(servidor.getPorta().toString());
        }

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Configure a URL e a Porta")
                .setView(dialogView)
                .setCancelable(false) // Impede o fechamento ao clicar fora do diálogo
                .create();

        // Configurar manualmente o botão "Salvar"
        dialogView.findViewById(R.id.buttonSalvar).setOnClickListener(v -> {
            String url = editTextUrl.getText().toString().trim();
            String portStr = editTextPort.getText().toString().trim();

            if (url.isEmpty()) {
                Toast.makeText(MainActivity.this, "Informar URL.", Toast.LENGTH_LONG).show();
                return; // Não fecha o diálogo
            }

            if (portStr.isEmpty()) {
                Toast.makeText(MainActivity.this, "Informar Porta.", Toast.LENGTH_LONG).show();
                return; // Não fecha o diálogo
            }

            try {
                Long port = Long.valueOf(portStr);

                Servidor servidorSalvar = new Servidor();
                servidorSalvar.setUrl(url);
                servidorSalvar.setPorta(port);

                // Salva o servidor no banco de dados
                DataBase.getInstance(MainActivity.this).servidorDao().inserir(servidorSalvar).get();

                servidor = servidorSalvar;
                fetchProximityData();
                fetchGyroData();

                // Fechar o diálogo após salvar com sucesso
                dialog.dismiss();

            } catch (NumberFormatException e) {
                Toast.makeText(MainActivity.this, "Porta inválida. Informe um número.", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, "Erro ao salvar no banco de dados.", Toast.LENGTH_LONG).show();
            }
        });

        // Configurar manualmente o botão "Cancelar"
        dialogView.findViewById(R.id.buttonCancelar).setOnClickListener(v -> {
            if(servidor == null) {
                finish(); // Fecha a Activity e a aplicação
            }else {
                dialog.dismiss();
            }
        });

        // Exibir o diálogo
        dialog.show();
    }

    private void fetchProximityData() {
        if (servidor == null) {
            Toast.makeText(this, "Servidor não configurado. Configure a URL e a porta primeiro.", Toast.LENGTH_LONG).show();
            return;
        }

        ApiService apiService = ApiClient.getInstance(servidor).getService();
        apiService.getProximityData().enqueue(new Callback<ProximityResponse>() {
            @Override
            public void onResponse(Call<ProximityResponse> call, Response<ProximityResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Entry> proximityEntries = new ArrayList<>();
                    List<ProximityData> proximityData = response.body().getProximidade();

                    // Iterar pela lista na ordem inversa
                    for (int i = proximityData.size() - 1; i >= 0; i--) {
                        ProximityData data = proximityData.get(i);
                        // O índice agora será recalculado para manter a ordem visual correta
                        int index = proximityData.size() - 1 - i;
                        proximityEntries.add(new Entry(index, Float.parseFloat(data.getValor())));
                    }

                    LineDataSet proximityDataSet = new LineDataSet(proximityEntries, "Proximidade");
                    LineData proximityData2 = new LineData(proximityDataSet);
                    proximityChart.setData(proximityData2);
                    proximityChart.invalidate(); // Refresh chart
                } else {
                    Toast.makeText(MainActivity.this, "Falha ao buscar dados de proximidade.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProximityResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Erro na chamada de API: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }



    private void fetchGyroData() {
        if (servidor == null) {
            Toast.makeText(this, "Servidor não configurado. Configure a URL e a porta primeiro.", Toast.LENGTH_LONG).show();
            return;
        }

        ApiService apiService = ApiClient.getInstance(servidor).getService();
        apiService.getGyroData().enqueue(new Callback<GyroResponse>() {
            @Override
            public void onResponse(Call<GyroResponse> call, Response<GyroResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<BarEntry> gyroEntries = new ArrayList<>();
                    List<GyroData> gyroData = response.body().getGiro();

                    // Iterar pela lista na ordem inversa
                    for (int i = gyroData.size() - 1; i >= 0; i--) {
                        GyroData data = gyroData.get(i);
                        // O índice agora será recalculado para manter a ordem visual correta
                        int index = gyroData.size() - 1 - i;
                        gyroEntries.add(new BarEntry(index, Float.parseFloat(data.getValor())));
                    }

                    BarDataSet gyroDataSet = new BarDataSet(gyroEntries, "Giro");
                    BarData gyroData2 = new BarData(gyroDataSet);
                    gyroChart.setData(gyroData2);
                    gyroChart.invalidate(); // Refresh the chart
                } else {
                    Toast.makeText(MainActivity.this, "Falha ao buscar dados de giro.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GyroResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Erro na chamada de API: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


}