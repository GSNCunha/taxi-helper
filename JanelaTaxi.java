import java.awt.*;
import javax.swing.*;
import javax.swing.JOptionPane;
import javax.swing.border.*;
import java.io.*;
import java.util.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.lang.NumberFormatException;
import java.lang.*;

import javax.swing.*;
import javax.swing.event.*;

import java.awt.*;
import java.awt.event.*;



 class JanelaTaxi extends JFrame implements DocumentListener,ActionListener
{
   private JPanel painel1, painel2, painel3, painel4, painel5, painel6;//painéis responsáveis por organizar todos os elementos da janela.
   private JTextArea tMaisprox,tPesqlogra;// caixas de texto onde vão ficar os pontos mais próximos do usuário e pontos em relação a um logradouro, respectivamente. 
   private JTextField tLat, tLong, tLogra,tErro;//pequnas caixas de texto onde vão entrar a latitude do usuário, longitude do usuário, logradouro informado pelo usuário e erros, respectivamente.
   private JButton procurar,pontos;// botão de procurar pontos mais pertos a partir da localização e para abrir o exel, respectivamente.
   private JLabel indLat, indLong, indLogra,indErro;//textos para indicar para que cada elemento na janela serve.
   public Vector<PontoTaxi> vetor;//vetor que vai reservar cada ponto de taxi
   public Vector<String> vetorLogradouro = new Vector<>();//vetor que vai auxiliar a impressão dos pontos de taxi a partir do logradouro
   public Vector<String> vErro= new Vector<>();//vetor que resserva erros

   public JanelaTaxi() 
   {
      super("Localizador de Taxi");//nome da janela

      Container container = getContentPane();//container que vi receber os elemento da janela

      indLat = new JLabel("Latitude:");//escrito de cada texto
      indLong = new JLabel("Longitude:");
      indLogra = new JLabel("Logradouro:");
      indErro = new JLabel("Erros:");

      procurar = new JButton("Procurar");  //escrito dos botões   
      pontos = new JButton("Todos os Pontos");

      tLat = new JTextField(10);//formatação das pequenas caixas de texto
      tLat.setForeground(Color.BLACK);
      tLong = new JTextField(10);
      tLong.setForeground(Color.BLACK);
      tLogra = new JTextField(30);
      tLogra.setForeground(Color.BLACK);
      tErro = new JTextField(50);
      tErro.setForeground(Color.BLACK);

      tMaisprox = new JTextArea(15,50);//formatação das caixas de texto
      tPesqlogra = new JTextArea(15,50);
      tMaisprox.setEditable(false);//são apenas informativas, não precisam ser editáveis
      tPesqlogra.setEditable(false);

      painel1 = new JPanel();
      painel2 = new JPanel();
      painel3 = new JPanel();
      painel4 = new JPanel();
      painel5 = new JPanel();
      painel6 = new JPanel();

      painel1.setLayout(new FlowLayout(FlowLayout.CENTER));//centralizar os elementos para uma boa formatação da janela
      painel2.setLayout(new FlowLayout(FlowLayout.CENTER));
      painel3.setLayout(new FlowLayout(FlowLayout.CENTER));
      painel4.setLayout(new FlowLayout(FlowLayout.CENTER));
      painel5.setLayout(new FlowLayout(FlowLayout.CENTER));
      painel6.setLayout(new BoxLayout(painel6, BoxLayout.Y_AXIS));//os elementos do painel 6 vão ficar um cima do outro


      painel1.add(indLat);//adicionar cada elemento de cada painel
      painel1.add(tLat);
      painel1.add(indLong);
      painel1.add(tLong);
      painel1.add(procurar);

      painel2.add(tMaisprox);

      painel3.add(indLogra);
      painel3.add(tLogra);

      painel4.add(tPesqlogra);

      painel5.add(indErro);
      painel5.add(tErro);
      painel5.add(pontos);

      painel6.add(painel2);// o painel 6 serve apenas para organizar os outros(2, 3 e 4 ) um em cima do outro.
      painel6.add(painel3);
      painel6.add(painel4);

      container.add(painel1);
      container.add(painel6);
      container.add(painel5);

      container.add(painel1,BorderLayout.NORTH);//assim todos ficam um em cima do outro
      container.add(painel5,BorderLayout.SOUTH);
      container.add(painel6,BorderLayout.CENTER);

      this.pack();
      this.setVisible(true);

      pontos.setActionCommand("pontos");
      procurar.setActionCommand("procurar");
      tLogra.setActionCommand("Logradouro");

      pontos.addActionListener(this);
      procurar.addActionListener(this);
      tLogra.getDocument().addDocumentListener(this);

      try(BufferedReader leitor = new BufferedReader(new FileReader("pontos_taxi.csv")))// nessa hora pode haver um erro de leitura de arquivo
      {
         String linha = null;
         leitor.readLine();
         vetor = new Vector<>();
         while((linha = leitor.readLine()) !=null)// reservamos as infomações dos pontos em uma struct/classe e resevamos elas em serie em um vetor
         {
            String[] data = linha.split(";");
            data[5] = data[5].substring(1, data[5].length() - 1);//tirar aspas
            PontoTaxi umPonto = new PontoTaxi(data[0],data[1],data[2],data[3],data[4],data[5],data[6],data[7]);
            vetor.add(umPonto);
         }
      }
      catch(IOException ea)
      {
         tErro.setText("erro ao ler o arquivo!");//caso haja um erro de leitura de arquivo, isso é notificado
      }
   }
   public void insertUpdate(DocumentEvent aa)// se houver um evento aqui, o usuário esta escrevendo o logradouro
   {
      tPesqlogra.setText("");
      for(int x = 0; x < vetor.size(); x++)//varremos todos os logradouros procurando semelhantes
      {
         if(vetor.get(x).logradouro.contains(tLogra.getText().toUpperCase()))//vemos se o que o usuário escreveu esta contigo em algum logradouro existente//mudamos para upercase para que o usuario n sofra com diferenças de minuscula e maiuscula
         {
            vetorLogradouro.add("Nome:");//fazemos um vetor com informações importantes de todos os logradouros semelhantes
            vetorLogradouro.add(vetor.get(x).nome);
            vetorLogradouro.add("Numero:");
            vetorLogradouro.add(""+vetor.get(x).telefone+"\n");

         }
      }
      String texto = "";
      for( int j=0;j< vetorLogradouro.size();j++)
       {
         texto = texto + " " + vetorLogradouro.get(j);
       }
      tPesqlogra.setText(""+texto);
      vetorLogradouro.clear();
    }
   public void changedUpdate(DocumentEvent aa)// o mesmo do de cima 
   {
      tPesqlogra.setText("");
      for(int x = 0; x < vetor.size(); x++)
      {
         if(vetor.get(x).logradouro.contains(tLogra.getText().toUpperCase())) 
         {
            vetorLogradouro.add("Nome:");
            vetorLogradouro.add(vetor.get(x).nome);
            vetorLogradouro.add("Numero:");
            vetorLogradouro.add(""+vetor.get(x).telefone+"\n");

         }
      }
      String texto = "";
      for( int j=0;j< vetorLogradouro.size();j++)
       {
         texto = texto + " " + vetorLogradouro.get(j);
       }
      tPesqlogra.setText(""+texto);//printamos  na tela.
      vetorLogradouro.clear();
    }
   public void  removeUpdate(DocumentEvent aa)// o mesmo do de cima
   {
      if(!(tLogra.getText().isEmpty()))// a unica coisa diferente é que se o usuário apagar tudo que tinha escrito, temos que apagar as informações da tela
      {
         tPesqlogra.setText("");
         for(int x = 0; x < vetor.size(); x++)
         {
            if(vetor.get(x).logradouro.contains(tLogra.getText().toUpperCase())) 
            {
               vetorLogradouro.add("Nome:");
               vetorLogradouro.add(vetor.get(x).nome);
               vetorLogradouro.add("Numero:");
               vetorLogradouro.add(""+vetor.get(x).telefone+"\n");

            }
         }
         String texto = "";
         for( int j=0;j< vetorLogradouro.size();j++)
          {
            texto = texto + " " + vetorLogradouro.get(j);
          }
         tPesqlogra.setText(""+texto);
         vetorLogradouro.clear();
      }
      else
      {
         tPesqlogra.setText(" ");
      }
    }
   public void actionPerformed(ActionEvent ead) 
   {
      if (ead.getActionCommand().equals("procurar"))// se o botão precionar for prcionado
      {
           procuraPontos();
      }
      else
      {
         try
         {
            Desktop.getDesktop().open(new File("pontos_taxi.csv"));//caso o outro botão for precionado, tentamos abrir um exel, isso pode gerar um erro de IO!
         }catch(IOException a)
         {
            tErro.setText("Erro ao abrir o arquivo!");
         }
      }
   }     

   public void procuraPontos()
   {
      try//o usuário pode digitar a coordenada de um jeito que não deveria
      {
      String latitude = tLat.getText().replace(',', '.');// assim podemos digitar números com virgula ou ponto
      String longitude = tLong.getText().replace(',', '.');
      Double lat = Double.parseDouble(latitude);// transforma a string recebida em numero para calculos(pode dar exçeção) 
      Double lon = Double.parseDouble(longitude);   

      int prim = 0;//vamos procurar os tres mais próximos, chamamos uma função da classe/estruct para calcular a distancia
      int segu = 1;//depois disso ordenamos os 3 primeiros pontos 
      int terc = 2;
      if(vetor.get(segu).haversine(lat,lon) < vetor.get(prim).haversine(lat,lon))
      {
         prim = 1;
         segu = 0;
      }
      if(vetor.get(terc).haversine(lat,lon) < vetor.get(segu).haversine(lat,lon))
      {
         terc = segu;
         segu = 2;  
      }
      if(vetor.get(terc).haversine(lat,lon) < vetor.get(prim).haversine(lat,lon))
      {
         int reserva;
         reserva = terc;
         terc = prim;
         prim = reserva;  
      }

      for(int x = 3; x < vetor.size(); x++)// depois disso vemos cada vetor e caso ele seja menor que algum dos ja colocados em primeiro, segundo e terceiro, refazemos ou primeiros lugares contando ele
      {
         try
         {
            if(vetor.get(x).haversine(lat,lon) < vetor.get(terc).haversine(lat,lon))
            {
               if(vetor.get(x).haversine(lat,lon) < vetor.get(segu).haversine(lat,lon))
                  {
                      if(vetor.get(x).haversine(lat,lon) < vetor.get(prim).haversine(lat,lon))
                      {
                           int reserva;
                           reserva = segu;
                           segu = prim;
                           terc = reserva; 
                           prim = x;
                      }
                      else
                      {
                        terc = segu;
                        segu = x;
                      }
                  }
                  else 
                  {
                     terc = x;
                  }
            }
         }
         catch(NumberFormatException a)// existem erros nas coordenadas no aruivo dado, aqui imprimimos as linhas com erro
         {
            String texto = "";
            vErro.add("Erro na coordenada da linha: "+(x+2)+" ");
            for( int j=0;j< vErro.size();j++)
            {
               texto = texto + " " + vErro.get(j);
            }
            tErro.setText(""+texto);
         }
      }
     // tMaisprox.setText(""+String.valueOf(vetor.get(prim).haversine(lat,lon))+"\n"+String.valueOf(vetor.get(segu).haversine(lat,lon))+"\n"+String.valueOf(vetor.get(terc).haversine(lat,lon)));
      tMaisprox.setText("PRIMEIRO PONTO:\nNome:"+vetor.get(prim).nome+";\nLogradouro:"+vetor.get(prim).logradouro+";\nNumero:"+vetor.get(prim).numero+";\nDistancia:"+String.valueOf(vetor.get(prim).haversine(lat,lon))+"metros.\n"+"SEGUNDO PONTO:\nNome:"+vetor.get(segu).nome+";\nLogradouro:"+vetor.get(segu).logradouro+";\nNumero:"+vetor.get(segu).numero+";\nDistancia:"+String.valueOf(vetor.get(segu).haversine(lat,lon))+"metros.\n"+"TERCEIRO PONTO:\nNome:"+vetor.get(terc).nome+";\nLogradouro:"+vetor.get(terc).logradouro+";\nNumero:"+vetor.get(terc).numero+";\nDistancia:"+String.valueOf(vetor.get(terc).haversine(lat,lon))+"metros.");
      }//aqui imprimos os 3 mais próximos 
      catch(NumberFormatException a)
      {
         tErro.setText("coordenadas dadas estão erradas!");// caso o usuário bote uma coordenada errada
      }
   }
}