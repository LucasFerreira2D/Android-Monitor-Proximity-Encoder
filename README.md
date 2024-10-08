# Android-Monitor-Proximity-Encoder

## Aplicativo Android - Monitoramento de Dados de Proximidade e Giroscópio

Este aplicativo Android foi desenvolvido para monitorar e exibir dados de proximidade e giroscópio usando uma API RESTful. Ele permite que os usuários configurem a URL e a porta de um servidor, recuperem dados e os visualizem em tempo real usando gráficos.

## 1. Funcionalidades
* Configurar URL e porta do servidor para conexão com a API.
* Buscar dados de proximidade e exibi-los em um gráfico de linha.
* Buscar dados de giroscópio e exibi-los em um gráfico de barras.
* Visualização de dados em tempo real.
* Tratamento de erros e validação para entrada de URL e porta.
* Interface amigável com diálogos de configuração.

![image](https://github.com/user-attachments/assets/22ce11f5-0acb-4539-8cc6-8ace24a1b262)

![image](https://github.com/user-attachments/assets/a49a5f6e-a60e-4876-97e3-ad91f258be9d)

## 2. Requisitos
* Android Studio (Versão mais recente)
* Android SDK versão 21 ou superior
* Java 8+
* Conexão com a internet para comunicação com a API

## 3. Instalação
1. Clone o repositório:
   ```bash
   git clone https://github.com/LucasFerreira2D/Android-Monitor-Proximity-Encoder.git
   ```
2. Abra o projeto no Android Studio.
3. Sincronize o projeto com os arquivos Gradle.
4. Conecte um dispositivo Android ou use um emulador para rodar o app.

## 4. Configuração

### 4.1. Configurando o Servidor
Antes de usar o app, é necessário configurar os detalhes do servidor (URL e porta):
1. Abra o app.
2. Se o servidor ainda não estiver configurado, um diálogo aparecerá solicitando que você insira a URL e a Porta.
3. Insira a URL e a porta do servidor e clique em Salvar.
   * **URL**: Deve ser o endereço da sua API RESTful (ex.: 192.168.1.10).
   * **Porta**: Deve ser o número da porta onde a API está rodando (ex.: 8080).
4. O app usará os detalhes fornecidos para todas as interações com a API.

### 4.2. Alterando a Configuração do Servidor
Caso precise alterar os detalhes do servidor posteriormente, você pode acessar a tela de configurações no app e atualizar a URL e a Porta.

## 5. Documentação da API
O app se comunica com uma API RESTful para recuperar os dados. Abaixo está uma breve descrição dos endpoints:

### 5.1. Endpoint para Dados de Proximidade:
* **URL**: `http://<url_do_servidor>:<porta>/proximidade`
* **Método**: GET
* **Resposta**: Objeto JSON contendo os dados de proximidade.

### 5.2. Endpoint para Dados de Giroscópio:
* **URL**: `http://<url_do_servidor>:<porta>/giro`
* **Método**: GET
* **Resposta**: Objeto JSON contendo os dados de giroscópio.

Certifique-se de que seu servidor expõe esses endpoints com o formato de dados apropriado antes de usar o app.

## 6. Exemplo de Resposta
### 6.1. Resposta para Dados de Proximidade:
```json
{
  "proximidade": [
    {
      "valor": "12.5"
    },
    {
      "valor": "13.0"
    }
  ]
}
```

### 6.2. Resposta para Dados de Giroscópio:
```json
{
  "giro": [
    {
      "valor": "0.5"
    },
    {
      "valor": "0.6"
    }
  ]
}
```
