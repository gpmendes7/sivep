package app.copias;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import modelo.Notificacao;

public class IdentificarNotificaoesCopias {
	
	private static FileWriter fileWriter;
	
	public static void main(String[] args) throws IOException {
		fileWriter = new FileWriter("./resultados/copias/copias.txt");
		
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("sivep");
		EntityManager em = emf.createEntityManager();
		
		String jpql= "select n from Notificacao n";
		
		TypedQuery<Notificacao> query = em.createQuery(jpql, Notificacao.class);
		
		List<Notificacao> notificacoes = query.getResultList();
		
		int totalDeNotificacoesComCopias = 0;
		int totalCopias = 0;
		while(!notificacoes.isEmpty()) { 
			  Notificacao notificacao = notificacoes.remove(0);
		  
			  List<Notificacao> notificacoesCopia = notificacoes.stream() 
										                        .filter(n -> n.ehCopiaDe(notificacao))
										                        .collect(Collectors.toList());
			  
			  if(notificacoesCopia.size() > 0) {
				  totalDeNotificacoesComCopias++;
				  
				  for (Notificacao notificacaoCopia : notificacoesCopia) {
					  fileWriter.write("***************************\n");
					  fileWriter.write("Notificações com o mesmo nome completo, data de notificação, classificação final, evolução caso, data de internação e data de encerramento\n");
					  fileWriter.write(notificacao + "\n");
					  fileWriter.write(notificacaoCopia + "\n");
					  fileWriter.write("***************************\n");
				  }
				  
				  totalCopias += notificacoesCopia.size();
				  notificacoes.removeAll(notificacoesCopia); 
			  }		  						  		  
		}
		
		System.out.println("Total de notificações com cópias: " + totalDeNotificacoesComCopias);
		System.out.println("Total de cópias identificadas: " + totalCopias);
		 
		em.close();
		emf.close();
		
		fileWriter.close();
	}

}
