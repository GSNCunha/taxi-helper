public class PontoTaxi
{
	public String data_extracao;// informações dos pontos
	public String codigo;
	public String nome;
	public String telefone;
	public String logradouro;
	public String numero;
	public String latitude;
	public String longitude;
	public double R = 6372.8;//valor necessário no calculo da distancia

	public PontoTaxi(String data_extracao,String codigo,String nome,String telefone,String logradouro,String numero,String latitude,String longitude)
	{
	this.data_extracao = data_extracao;
	this.codigo = codigo;
	this.nome = nome;
	this.telefone = telefone;
	this.logradouro = logradouro;
	this.numero = numero;
	this.latitude = latitude;
	this.longitude = longitude;
	}

    public  double haversine(double lat2, double lon2) {//calculo da distancia
    	latitude = latitude.replace(',', '.');
    	longitude = longitude.replace(',', '.');
        double lat1 = Double.parseDouble(latitude);
        double lon1 = Double.parseDouble(longitude);

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);
 
        double a = Math.pow(Math.sin(dLat / 2),2) + Math.pow(Math.sin(dLon / 2),2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return R * c;
    }
}