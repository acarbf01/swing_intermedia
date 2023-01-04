package swing_intermedia;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Flushable;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

public class Ventana extends JFrame implements ActionListener {
	// Declaracion variables
	private Scanner leer;
	private JPanel panel;
	private JMenuBar barra;
	private JMenu menuSol;
	private JMenu menuPlay;
	private JMenuItem abrir;
	private JMenuItem crear;
	private JMenuItem guardar;
	private JMenuItem guardarComo;
	private JMenuItem jugar;
	private JMenuItem deshacer;
	private JMenuItem rehacer;
	private JMenuItem ayuda;
	private JOptionPane mssg;
	private Boton inicio;
	private Boton fin;

	private int[][] matriz;

	private Historial historial;
	private HistorialJuego historialJuego;

	private boolean jugando;

	private String[][] mj;

	String ruta = "";

	public Ventana() {
		// inicializacion variables
		crearPanel();
		abrir = new JMenuItem("Abrir");
		abrir.addActionListener(this);
		crear = new JMenuItem("Crear");
		crear.addActionListener(this);
		guardar = new JMenuItem("Guardar");
		guardar.addActionListener(this);
		guardarComo = new JMenuItem("Guardar Como");
		guardarComo.addActionListener(this);
		jugar = new JMenuItem("Jugar");
		jugar.addActionListener(this);
		deshacer = new JMenuItem("Deshacer");
		deshacer.addActionListener(this);
		rehacer = new JMenuItem("Rehacer");
		rehacer.addActionListener(this);
		ayuda = new JMenuItem("Ayuda");
		ayuda.addActionListener(this);

		historial = new Historial();
		historialJuego = new HistorialJuego();
		// Añado menus a la barra menu
		this.menuSol = anhadirAlMenu(abrir, crear, guardar, guardarComo);
		menuSol.setText("Opciones");
		this.menuPlay = anhadirAlMenu(jugar, deshacer, rehacer, ayuda);
		menuPlay.setText("Acciones");
		crearBarra(menuSol, menuPlay);

		// Doy valores y ajusto tamaño de la ventana
		this.setLayout(new BorderLayout());
		this.setTitle("Editar");
		this.setSize(1080, 720);
		this.add(panel, BorderLayout.CENTER);
		this.setJMenuBar(barra);

		// Cerrar proceso al salir
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	// Metodo que realiza las acciones de las diferentes opciones
	public void actionPerformed(ActionEvent i) {
		// TODO Auto-generated method stub
		if (i.getSource() == crear) {
			ruta = "";
			historial = new Historial();
			jugando = false;
			crearOption(Integer.parseInt(mssg.showInputDialog("Introduce el tamaño de las filas.")),
					Integer.parseInt(mssg.showInputDialog("Introduce el tamaño de las columnas.")));
		} else if (i.getSource() == guardar) {
			guardar();
		} else if (i.getSource() == guardarComo) {
			guardarComo();
		} else if (i.getSource() == abrir) {
			try {
				jugando= false;
				historial = new Historial();
				abrir();
			} catch (Exception e) {
				// Cambiar por JOptionPane
				e.printStackTrace();
			}
		} else if (i.getSource() == deshacer) {
			if (jugando) {
				historialJuego.deshacer();
			} else {
				historial.deshacer();
			}
		} else if (i.getSource() == rehacer) {
			if (jugando) {
				historialJuego.rehacer();
			} else {
				historial.rehacer();
			}
		} else if (i.getSource() == jugar) {
			jugando = true;
			jugar();
		}else if(i.getSource() == ayuda) {
			ayuda();
		}

	}

	private void ayuda() {
		// TODO Auto-generated method stub
		SwingWorker<String, Void> worker = new SwingWorker<String, Void>(){

			@Override
			protected String doInBackground() throws Exception {
				// TODO Auto-generated method stub
				ArrayList<ArrayList<Integer>> x = new ArrayList<>();
				for (int i = 0; i < matriz.length; i++) {
					ArrayList<Integer> aux = new ArrayList<>(); 
					for (int j = 0; j < matriz[0].length; j++) {
						aux.add(matriz[i][j]);
					}
					x.add(aux);
				}
				
				Camino camino = new Camino(x, x.get(0).size(), x.size());
				ArrayList<ArrayList<Integer>> caminoAux = new ArrayList<ArrayList<Integer>>();
				ArrayList<Integer> coord = new ArrayList<Integer>();
				String[][] matrizImprime = new String[( x.size() * 2) - 1][( x.get(0).size() * 2) - 1];
				int line = 0;
				int col = 0;
				for (int u = 0; u <  x.size() * 2 - 1; u++) {
					for (int v = 0; v <  x.get(0).size() * 2 - 1; v++) {
						if (v % 2 != 0 || u % 2 != 0) {
							matrizImprime[u][v] = " ";
						} else {
							matrizImprime[u][v] = camino.getMatriz().get(line).get(col).toString();
							col++;
						}
					}
					col = 0;
					if (!matrizImprime[u][0].equals(" "))
						line++;
				}
				camino.buscaCamino(caminoAux, x.get(0).get(0), coord, 0, 0, matrizImprime, 0, camino.getCaminoRec());

				String[][] firstSol= camino.getSolution(0);
				if(firstSol == null) {
					JOptionPane.showMessageDialog(null, "No hay solución");
					
				}else {
					
					
					mj = firstSol;
					for (int i = 0; i < mj.length; i++) {
						for (int j = 0; j < mj[0].length; j++) {
							if(mj[i][j].equals("") || mj[i][j].equals(" ")) {
								mj[i][j] = null;
							}
						}
					}
					cargarPanelJuego();
				}
				return null;
			}
			
		};
		worker.execute();
	}

	public void crearPanel() {
		panel = new JPanel();
	}

	public void crearBarra(JMenu... cosa) {
		barra = new JMenuBar();
		for (int i = 0; i < cosa.length; i++)
			barra.add(cosa[i]);
	}

	public void anhadirAlPanel(Component... cosas) {
		for (int i = 0; i < cosas.length; i++)
			panel.add(cosas[i]);
	}

	public JMenu anhadirAlMenu(JMenuItem... items) {
		JMenu men = new JMenu();
		for (int i = 0; i < items.length; i++)
			men.add(items[i]);
		return men;
	}

	// Pide valores para crear matriz de solucion
	public void crearOption(int filas, int columnas) {
		try {
			if (filas < 1 || filas > 9 || columnas < 1 || columnas > 9)
				mssg.showMessageDialog(null, "Error, debes introducir un numero entre 1 y 9");
		} catch (Exception e) {
			mssg.showMessageDialog(null, "Error, debes introducir un numero entre 1 y 9");
		}
		matriz = new int[filas][columnas];
		this.panel.removeAll();
		this.panel.setLayout(new GridLayout(filas, columnas));
		for (int i = 0; i < matriz.length; i++) {
			for (int j = 0; j < matriz[0].length; j++) {
				JTextField txt = new JTextField();
				txt.setHorizontalAlignment(JTextField.CENTER);

				focus(i, j, txt);
				panel.add(txt);
			}
		}
		this.setVisible(true);
	}

	// Toma los valores de los recuadros de texto y comprueba que no sean caracteres
	// o que no este entre 1 y 9
	private void focus(int i, int j, JTextField txt) {
		txt.addFocusListener(new FocusAdapter() {
			private String state;

			public void focusLost(FocusEvent event) {
				try {
					matriz[i][j] = Integer.parseInt(txt.getText());
					if (matriz[i][j] < 1 || matriz[i][j] > 9) {
						mssg.showMessageDialog(null, "Error, debes introducir un numero entre 1 y 9");
						matriz[i][j] = 0;
						txt.setText("");
					}
				} catch (Exception e) {
					if (!txt.getText().equals("")) {
						mssg.showMessageDialog(null, "Error, debes introducir un numero entre 1 y 9");
						txt.setText("");
					}
				}
				int[][] matrizAux = new int[matriz.length][matriz[0].length];
				if (!state.equals(txt.getText())) {
					// CLONAR
					getMatriz(matrizAux);
					// Si state = "" matriz[i][j] = 0, si no es state
					if (state.equals("")) {
						matrizAux[i][j] = 0;
					} else {
						matrizAux[i][j] = Integer.parseInt(state);
					}

					historial.add(matrizAux);
				}
			}

			@Override
			public void focusGained(FocusEvent event) {
				state = txt.getText();

			}

		});
	}

	public void getMatriz(int[][] matrix) {
		for (int i = 0; i < matriz.length; i++) {
			for (int j = 0; j < matriz[0].length; j++) {
				matrix[i][j] = matriz[i][j];
			}
		}
	}

	public void guardar() {
//Comprueba si la ruta esta vacia para que se especifique la ruta 
//(Se realiza en guardarComo, por eso se le llama)
		if (ruta.equals("")) {
			guardarComo();
		}
		try {
			// El ""PAPEL""
			FileWriter archivo = new FileWriter(ruta);
			// El ""BOLI""
			PrintWriter escribo = new PrintWriter(archivo);

			// Escribimos la matriz en el archivo
			for (int i = 0; i < matriz.length; i++) {
				for (int j = 0; j < matriz[0].length; j++) {
					escribo.print(matriz[i][j] + " ");
				}
				escribo.println("");
			}
			// cerramos el archivo
			archivo.close();
			// Tratamos las excepciones
		} catch (IOException e) {
			if (e.getMessage().contains("FileWriter")) {
				mssg.showMessageDialog(null, "Error creando el archivo.");
			}
			mssg.showMessageDialog(null, "Error de escritura.");
		}
	}

	public void guardarComo() {
		// Variable objeto ventana archivos
		JFileChooser ventGuarda = new JFileChooser();
		// Variable que muestra la ventana de guardado y guarda el resultado de la
		// seleccion
		int result = ventGuarda.showSaveDialog(null);

		try {
			// Si se selcciona guardado
			if (result == 0) {
				File file = ventGuarda.getSelectedFile();
				this.ruta = file.getAbsolutePath();
				// Si ya tiene .txt en la ruta no se la tengo que volver a añadir
				if (!ruta.endsWith(".txt"))
					this.ruta += ".txt";
				// Como la ruta ha cambiado hay que iniciarlo de nuevo
				file = new File(ruta);
				// Aqui comprobamos si existe el fichero o no. Usar File para comprobar
				if (file.exists()) {
					int resp = mssg.showConfirmDialog(null, "¿Quiere sobreescribir el archivo?");
					// opción que quiere sobreescribir (resp == 0)
					if (resp == 0) {
						guardar();
					} else {
						// Cierra la ventana al no querer sobreescribir
						ventGuarda.cancelSelection();
					}
				} else {
					// Si no existe el archivo guardamos normal
					guardar();
				}
			}
		} catch (Exception e) {
			mssg.showMessageDialog(null, "Error en la lectura");
		}
	}

	// Metodo abrir
	@SuppressWarnings("resource")
	public void abrir() throws Exception {
		// Declaro Variables
		JFileChooser obtArch = new JFileChooser();
		FileReader leer;
		BufferedReader data;
		File file;
		String line;
		ArrayList<String> almLine = new ArrayList<String>();
		int[][] matriz;
		int filas;
		int columnas;
		try {
			// Ejecuto la accion si se abre algun archivo
			if (obtArch.showOpenDialog(null) == 0) {
				file = obtArch.getSelectedFile();
				leer = new FileReader(file);
				data = new BufferedReader(leer);
				// Añado las lineas del archivo en el ArrayList
				while ((line = data.readLine()) != null) {
					almLine.add(line);
				}
				filas = almLine.size();
				columnas = almLine.get(0).split(" ").length;
				// Comprobar que tenga el mismo numero de columnas
				for (int k = 1; k < filas; k++) {
					if (columnas != almLine.get(k).split(" ").length)
						throw new Exception("La matriz debe tener siempre el mismo numero de columnas");
				}
				matriz = new int[filas][columnas];
				// Añado los valores del ArrayList a una matriz de integers
				for (int i = 0; i < filas; i++) {
					for (int j = 0; j < columnas; j++) {
						matriz[i][j] = Integer.parseInt(almLine.get(i).split(" ")[j]);
						if (matriz[i][j] < 0 || matriz[i][j] > 9)
							throw new Exception(
									"Los valores de la matriz a abrir tienen que cumplir las normas de juego.");
					}
				}
				// LLamo al metodo que muestra los valores por pantalla
				verAbrir(matriz);
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	public void verAbrir(int[][] matriz) {
		this.panel.removeAll();
		this.panel.setLayout(new GridLayout(matriz.length, matriz[0].length));
		for (int i = 0; i < matriz.length; i++) {
			for (int j = 0; j < matriz[0].length; j++) {
				JTextField txt = new JTextField();
				txt.setHorizontalAlignment(JTextField.CENTER);

				// Add el contenido de la matriz al JTextField
				if (matriz[i][j] != 0) {
					txt.setText("" + matriz[i][j]);
				}
				focus(i, j, txt);
				panel.add(txt);
			}
		}
		// Nos falta guardarla en la variable global
		this.matriz = matriz;
		setVisible(true);
	}

	public void jugar() {
		int filas = matriz.length * 2 - 1;
		int columnas = matriz[0].length * 2 - 1;
		this.mj = new String[filas][columnas];
		for (int i = 0; i < mj.length; i += 2) {
			for (int j = 0; j < mj[0].length; j += 2) {
				this.mj[i][j] = "" + this.matriz[i / 2][j / 2];
			}
		}
		cargarPanelJuego();
	}

	public void cargarPanelJuego() {
		panel.removeAll();
		panel.setLayout(new GridLayout(mj.length, mj[0].length));
		for (int i = 0; i < mj.length; i++) {
			for (int j = 0; j < mj[0].length; j++) {
				if (i % 2 == 0 && j % 2 == 0) {
					Boton boton = new Boton(mj[i][j], i, j);
					panel.add(boton);
				} else {
					JTextField k = new JTextField();
					k.setEditable(false);
					k.setHorizontalAlignment(JTextField.CENTER);
					k.setFont(new Font("Agency FB", Font.BOLD, 40));
					if (mj[i][j] != null ) {
						k.setBackground(new Color(139,231,142));
						k.setText(mj[i][j]);
					}
					panel.add(k);
				}
			}
		}
		this.setVisible(true);
	}

	public int calculaMax() {
		int max = 0;
		for(int i = 0; i < matriz.length; i++) {
			for(int j = 0; j < matriz[0].length; j++) {
				if(matriz[i][j] > max)
					max = matriz[i][j];
			}
		}
		return max;
	}
	
	public int calculaMin() {
		int min = matriz[0][0];
		for(int i = 0; i < matriz.length; i++) {
			for(int j = 1; j < matriz[0].length; j++) {
				if(matriz[i][j] < min)
					min = matriz[i][j];
			}
		}
		return min;
	}
	class Historial {

		private ArrayList<int[][]> historial;
		private int contDes;

		public Historial() {
			this.historial = new ArrayList<>();
			this.contDes = 0;
		}

		public void add(int[][] matriz) {
			historial.add(matriz);
			contDes++;
		}

		public void deshacer() {
			comprobarMatriz();
			contDes--;
			if (contDes >= 0) {
				matriz = this.historial.get(contDes);
				verAbrir(this.historial.get(contDes));
			} else
				contDes = 0; // Falta mensaje
		}

		public void rehacer() {
			contDes++;
			if (contDes < this.historial.size()) {
				matriz = this.historial.get(contDes);
				verAbrir(historial.get(contDes));
			} else
				contDes--; // Falta mensaje
		}
		
		public void comprobarMatriz() {
			int pos = this.contDes;
			if(this.historial.size() == pos){
				pos--;
			}
			for(int i = 0; i < matriz.length; i++) {
				for(int j = 0; j < matriz[0].length; j++) {
					if(matriz[i][j] != this.historial.get(pos)[i][j]) {
						this.historial.add(matriz);
						break;
					}
				}
			}
		}
	}

	class HistorialJuego {

		private ArrayList<String[][]> historial;
		private int contDes;

		public HistorialJuego() {
			this.historial = new ArrayList<>();
			this.contDes = 0;
		}

		public void add(String[][] matriz) {
			String[][] m = new String[matriz.length][matriz[0].length];
			for(int i = 0; i < m.length; i++) {
				for(int j=0; j < m[0].length; j++) {
					m[i][j] = matriz[i][j];
				}
			}
			this.historial.add(m);
			contDes++;
		}

		public void deshacer() {
			comprobarMatriz();
			contDes--;
			if (contDes >= 0) {
				mj = this.historial.get(contDes);
				cargarPanelJuego();
			} else
				contDes = 0; // Falta mensaje
		}

		public void rehacer() {
			contDes++;
			if (contDes < this.historial.size()) {
				mj = this.historial.get(contDes);
				cargarPanelJuego();
			} else
				contDes--; // Falta mensaje
		}
		
		public void comprobarMatriz() {
			int pos = this.contDes;
			if(this.historial.size() == pos){
				pos--;
			}
			for(int i = 0; i < mj.length; i++) {
				for(int j = 0; j < mj[0].length; j++) {
					if(!Objects.equals(mj[i][j],this.historial.get(pos)[i][j])) {
						//Clonar Matriz
						this.historial.add(mj);
						break;
					}
				}
			}
		}
	}

	class Boton extends JButton implements ActionListener {

		private int fila;
		private int columna;

		public Boton(String string, int i, int j) {
			// TODO Auto-generated constructor stub
			this.setText(string);
			this.fila = i;
			this.columna = j;
			this.addActionListener(this);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if(inicio == null) {
				inicio = this;
			}else {
				fin = this;
				comprobarMov();
			}

		}
		
		public void comprobarMov() {
			//DiagonalSupIzq
			if(moverse(-2,-2, '\\') == true) {
				//Copiar y guardarla
				historialJuego.add(mj);
				mj[inicio.fila-1][inicio.columna-1] = "\\";
			}//Subir
			else if(moverse(-2,0, '|') == true) {
				historialJuego.add(mj);
				mj[inicio.fila-1][inicio.columna] = "|";
			}//DiagonalSupDer
			else if(moverse(-2,+2, '/') == true) {
				historialJuego.add(mj);
				mj[inicio.fila-1][inicio.columna+1] = "/";
			}//Izquierda
			else if(moverse(0,-2, '-') == true) {
				historialJuego.add(mj);
				mj[inicio.fila][inicio.columna-1] = "-";
			}//Derecha
			else if(moverse(0,+2, '-') == true) {
				historialJuego.add(mj);
				mj[inicio.fila][inicio.columna+1] = "-";
			}//DiagonalInfIzq
			else if(moverse(+2,-2, '/') == true) {
				historialJuego.add(mj);
				mj[inicio.fila+1][inicio.columna-1] = "/";
			}//Bajar
			else if(moverse(+2,0, '|') == true) {
				historialJuego.add(mj);
				mj[inicio.fila+1][inicio.columna] = "|";
			}//DiagonalInfDer
			else if(moverse(+2,+2, '\\') == true) {
				historialJuego.add(mj);
				mj[inicio.fila+1][inicio.columna+1] = "\\";
			}else {
				mssg.showMessageDialog(null,"Movimiento Invalido");
			}
			inicio = null;
			fin = null;
			cargarPanelJuego();
		}
		
		public boolean moverse(int movFila, int movCol, char mov) {
			//Recorrer movimientos
			if(inicio.fila + movFila == fin.fila && inicio.columna + movCol == fin.columna) {
				if(mj[inicio.fila + movFila/2][inicio.columna + movCol/2] == null) {
					if(Integer.parseInt(mj[inicio.fila][inicio.columna]) + 1 == Integer.parseInt(mj[fin.fila][fin.columna])) {
						return true;
					}else if(Integer.parseInt(mj[inicio.fila][inicio.columna]) == calculaMax() && Integer.parseInt(mj[fin.fila][fin.columna]) == calculaMin()) {
						return true;
					}
				}
			}
			return false;
		}
	}
}