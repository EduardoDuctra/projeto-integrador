//package br.projeto_integrador.aplicativo.backend.mercadoPago;
//
//import com.mercadopago.MercadoPagoConfig;
//import org.springframework.beans.factory.annotation.Autowired;
//
//public class Teste {
//    public static void main(String[] args) {
//
//        try {
//            MercadoPagoConfig.setAccessToken("TEST-5549217723115762-050308-7b749c3f3f9b8ac9b41fc4bd1aafe31c-3302682383");
//
//            PagamentoService pagamentoService = new PagamentoService();
//            String link = pagamentoService.criarPagamento(10.0, "01A");
//            System.out.println("Link gerado: " + link);
//
//        } catch (com.mercadopago.exceptions.MPApiException e) {
//
//            System.out.println("Status: " + e.getStatusCode());
//            System.out.println("Resposta: " + e.getApiResponse().getContent());
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}