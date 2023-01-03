package swing_intermedia;

import java.awt.BorderLayout;
import java.awt.Component;
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
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

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
	private Historial desre;

	private int[][] matriz;

	private Historial historial;

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
		deshacer = new JMenuItem("Deshacer");
		deshacer.addActionListener(this);
		rehacer = new JMenuItem("Rehacer");
		ayuda = new JMenuItem("Ayuda");

		historial = new Historial();
		// Añado menus a la barra menu
		this.menuSol = anhadirAlMenu(abrir, crear, guardar, guardarComo);
		menuSol.setText("Opciones");
		this.menuPlay = anhadirAlMenu(jugar, deshacer, rehacer, ayuda);
		menuPlay.setText("Acciones");
		crearBarra(menuSol, menuPlay);

		// Doy valores y ajusto tamaño de la ventana
		this.setLayout(new BorderLayout());
		this.setTitle("Titulo");
		this.setSize(1080, 720);
		this.add(panel, BorderLayout.NORTH);
		this.setJMenuBar(barra);

		// Cerrar proceso al salir
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	// Metodo que realiza las acciones de las diferentes opciones
	public void actionPerformed(ActionEvent i) {
		// TODO Auto-generated method stub
		if (i.getSource() == crear) {
			crearOption(Integer.parseInt(mssg.showInputDialog("Introduce el tamaño de las filas.")),
					Integer.parseInt(mssg.showInputDialog("Introduce el tamaño de las columnas.")));
		} else if (i.getSource() == guardar) {
			guardar();
		} else if (i.getSource() == guardarComo) {
			guardarComo();
		} else if (i.getSource() == abrir) {
			try {
				abrir();
			} catch (Exception e) {
				// Cambiar por JOptionPane
				e.printStackTrace();
			}
		} else if (i.getSource() == deshacer) {
			historial.deshacer();
		}

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
				if(!state.equals(txt.getText())){
					//CLONAR
						getMatriz(matrizAux);
						//Si state = "" matriz[i][j] = 0, si no es state
						if(state.equals("")) {
							matrizAux[i][j] = 0;
						}else {
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

	class Historial {

		private ArrayList<int[][]> historial;
		private int contDes;
		
		public Historial() {
			this.historial = new ArrayList<>();
			this.contDes= 0;
		}
		
		public void add(int[][]matriz) {
			historial.add(matriz);
			contDes++;
		}
		
		
		public void deshacer() {
			contDes--;
			if (contDes >= 0)
				verAbrir(historial.get(contDes));
			else contDes = 0; //Falta mensaje 
		}

		public void rehacer() {
			contDes++;
			if (contDes < historial.size())
				verAbrir(historial.get(contDes));
			else contDes--; //Falta mensaje 
		}
	}

}