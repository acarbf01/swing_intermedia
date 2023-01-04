package swing_intermedia;


import java.util.ArrayList;

public class Camino {

	private ArrayList<ArrayList<Integer>> matriz;
	private int mayor;
	private int menor;
	private int tamLinea;
	private int tamCol;
	private ArrayList<ArrayList<Integer>> camino;
	private boolean[][] caminoRec;
	private ArrayList<String [][]> soluciones;

	public Camino(ArrayList<ArrayList<Integer>> matriz, int tamLinea, int tamCol) {
		this.tamLinea = tamLinea;
		this.soluciones= new ArrayList<String [][]>();
		this.tamCol = tamCol;
		this.matriz = new ArrayList<ArrayList<Integer>>();
		for (ArrayList<Integer> m : matriz) {
			this.matriz.add((ArrayList<Integer>) m.clone());
		}
		mayor = buscaMayor();
		menor = buscaMenor();
		camino = new ArrayList<ArrayList<Integer>>();
		caminoRec = new boolean[tamCol][tamLinea];
		
	}

	public int getMayor() {
		return this.mayor;
	}

	public int getMenor() {
		return this.menor;
	}

	public int getTamLinea() {
		return this.tamLinea;
	}

	public int getTamCol() {
		return this.tamCol;
	}

	public ArrayList<ArrayList<Integer>> getMatriz() {
		return this.matriz;
	}

	public ArrayList<ArrayList<Integer>> getCamino() {
		return this.camino;
	}

	public boolean[][] getCaminoRec() {
		return this.caminoRec;
	}
	public ArrayList<String[][]> getSoluciones(){
		return this.soluciones;
	}


	private int buscaMayor() {
		int mayor = 0;
		for (int i = 0; i < matriz.size(); i++) {
			for (int j = 0; j < tamLinea; j++) {
				if (matriz.get(i).get(j) > mayor)
					mayor = matriz.get(i).get(j);
			}
		}
		return mayor;
	}

	private int buscaMenor() {
		int menor = matriz.get(0).get(0);
		for (int i = 0; i < getMatriz().size(); i++) {
			for (int j = 0; j < tamLinea; j++) {
				if (matriz.get(i).get(j) < menor)
					menor = matriz.get(i).get(j);
			}
		}
		return menor;
	}
	
	private void copia(String [][] matrizImprime, String [][] matrizDevuelve){
		for(int i = 0; i < tamCol * 2 - 1; i++) {
			for(int j = 0; j < tamLinea * 2 - 1; j++) {
				matrizDevuelve[i][j] = ""+  matrizImprime[i][j];
			}
		}	
	}

	public void buscaCamino(ArrayList<ArrayList<Integer>> camino, int numActual, ArrayList<Integer> coord, int i,
			int j, String[][]matrizImprime, int pasos, boolean[][] caminoRec) {
		int tamanho = tamCol*tamLinea;
		String [][] matrizN = new String[(tamCol * 2) - 1][(tamLinea * 2) - 1];
		boolean [][] caminoRecN = new boolean[caminoRec.length][caminoRec[0].length];
		caminoRec[0][0] = true;
		if (numActual == mayor) {
			numActual = menor - 1;
		}
		if(pasos == tamanho-1 && i == this.tamCol-1 && j == this.tamLinea-1 ) {
			soluciones.add(matrizImprime);
			return;
		}
		// Comprueba que no estamos en la primera linea ni en la primer columna para
		// comprobar hacia diagonal superior izqu
		if (j != 0 && i != 0) {
			if (matriz.get(i - 1).get(j - 1) == numActual + 1) {
				if (caminoRec[i - 1][j - 1] == false) {
					if (matrizImprime[i * 2 - 1][j * 2 - 1].equals(" ")) {
						coord = new ArrayList<Integer>();
						coord.add(i - 1);
						coord.add(j - 1);
						camino.add(coord);
						copia(matrizImprime, matrizN);
						copiaRec(caminoRec, caminoRecN);
						matrizN[i * 2 - 1][j * 2 - 1] = "\\";
						caminoRecN[i - 1][j - 1] = true;
						buscaCamino(camino, numActual+1, coord, coord.get(0), coord.get(1),matrizN, pasos+1, caminoRecN);

					}
				}
			}
		}
		// Comprueba que no estamos en la primera columna para comprobar hacia arriba
				if (i != 0) {
					if (matriz.get(i - 1).get(j) == numActual + 1) {
						if (caminoRec[i - 1][j] == false) {
							if (matrizImprime[i * 2 - 1][j * 2].equals(" ")) {
								coord = new ArrayList<Integer>();
								coord.add(i - 1);
								coord.add(j);
								camino.add(coord);
								copia(matrizImprime, matrizN);
								matrizN[i * 2 - 1][j * 2] = "|";
								copiaRec(caminoRec, caminoRecN);
								caminoRecN[i - 1][j] = true;
								buscaCamino(camino, numActual+1, coord, coord.get(0), coord.get(1),matrizN, pasos+1, caminoRecN);

							}
						}
					}
				}
				// Comprueba que no estamos en la ultima linea y primera columna , para
				// comprobar hacia diagonal superior derecha
				if (j != tamLinea - 1 && i != 0) {
					if (matriz.get(i - 1).get(j + 1) == numActual + 1) {
						if (caminoRec[i - 1][j + 1] == false) {
							if (matrizImprime[i * 2 - 1][j * 2 + 1].equals(" ")) {
								coord = new ArrayList<Integer>();
								coord.add(i - 1);
								coord.add(j + 1);
								camino.add(coord);
								copia(matrizImprime, matrizN);
								matrizN[i * 2 - 1][j * 2 + 1] = "/";
								copiaRec(caminoRec, caminoRecN);
								caminoRecN[i - 1][j + 1] = true;
								buscaCamino(camino, numActual+1, coord, coord.get(0), coord.get(1),matrizN, pasos+1, caminoRecN);
							}
						}
					}
				}
		// Comprueba que no estamos en la primera linea para comprobar hacia izq
				if (j != 0) {
					if (matriz.get(i).get(j - 1) == numActual + 1) {
						if (caminoRec[i][j - 1] == false) {
							if (matrizImprime[i * 2][j * 2 - 1].equals(" ")) {
								coord = new ArrayList<Integer>();
								coord.add(i);
								coord.add(j - 1);
								camino.add(coord);
								copia(matrizImprime, matrizN);
								matrizN[i * 2][j * 2 - 1] = "-";
								copiaRec(caminoRec, caminoRecN);
								caminoRecN[i][j - 1] = true;
								buscaCamino(camino, numActual+1, coord, coord.get(0), coord.get(1),matrizN, pasos+1, caminoRecN);

							}
						}
					}
				}
				// Comprueba que no estamos en la ultima linea para comprobar hacia derecha
				if (j != tamLinea - 1) {
					if (matriz.get(i).get(j + 1) == numActual + 1) {
						if (caminoRec[i][j + 1] == false) {
							if (matrizImprime[i * 2][j * 2 + 1].equals(" ")) {
								coord = new ArrayList<Integer>();
								coord.add(i);
								coord.add(j + 1);
								camino.add(coord);
								copia(matrizImprime, matrizN);
								matrizN[i * 2][j * 2 + 1] = "-";
								copiaRec(caminoRec, caminoRecN);
								caminoRecN[i][j + 1] = true;
								buscaCamino(camino, numActual+1, coord, coord.get(0), coord.get(1),matrizN, pasos+1, caminoRecN);

							}
						}
					}
				}
				// Comprueba que no estamos en la ultima linea y primera columna , para
				// comprobar hacia diagonal inferior izquierda
				if (j != 0 && i != tamCol - 1) {
					if (matriz.get(i + 1).get(j - 1) == numActual + 1) {
						if (caminoRec[i + 1][j - 1] == false) {
							if (matrizImprime[i * 2 + 1][j * 2 - 1].equals(" ")) {
								coord = new ArrayList<Integer>();
								coord.add(i + 1);
								coord.add(j - 1);
								camino.add(coord);
								copia(matrizImprime, matrizN);
								matrizN[i * 2 + 1][j * 2 - 1] = "/";
								copiaRec(caminoRec, caminoRecN);
								caminoRecN[i + 1][j - 1] = true;
								buscaCamino(camino, numActual+1, coord, coord.get(0), coord.get(1),matrizN, pasos+1, caminoRecN);

							}
						}
					}
				}
				// Comprueba que no estamos en la ultima columna para comprobar hacia abajo
				if (i != tamCol - 1) {
					if (matriz.get(i + 1).get(j) == numActual + 1) {
						if (caminoRec[i + 1][j] == false) {
							if (matrizImprime[i * 2 + 1][j * 2].equals(" ")) {
								coord = new ArrayList<Integer>();
								coord.add(i + 1);
								coord.add(j);
								camino.add(coord);
								copia(matrizImprime, matrizN);
								matrizN[i * 2 + 1][j * 2] = "|";
								copiaRec(caminoRec, caminoRecN);
								caminoRecN[i + 1][j] = true;
								buscaCamino(camino, numActual+1, coord, coord.get(0), coord.get(1),matrizN, pasos+1, caminoRecN);

							}
						}
					}
				}
		// Comprueba que no estamos en la ultima linea y ultima columna , para comprobar
		// hacia diagonal inferior derecha
		if (i != tamCol - 1 && j != tamLinea - 1) {
			if (matriz.get(i + 1).get(j + 1) == numActual + 1) {
				if (caminoRec[i + 1][j + 1] == false) {
					if (matrizImprime[i * 2 + 1][j * 2 + 1].equals(" ")) {
						coord = new ArrayList<Integer>();
						coord.add(i + 1);
						coord.add(j + 1);
						camino.add(coord);
						copia(matrizImprime, matrizN);
						matrizN[i * 2 + 1][j * 2 + 1] = "\\";
						copiaRec(caminoRec, caminoRecN);
						caminoRecN[i + 1][j + 1] = true;
						buscaCamino(camino, numActual+1, coord, coord.get(0), coord.get(1),matrizN, pasos+1, caminoRecN);
					}
				}
			}
		}
	}

	private void copiaRec(boolean[][] caminoRec, boolean[][] caminoRecN) {
		for(int i= 0; i < caminoRec.length; i++) {
			for(int j = 0; j < caminoRec[0].length; j++) {
				caminoRecN[i][j] = caminoRec[i][j];
			}
		}
	}

	public String[][] getSolution(int i) {
		// TODO Auto-generated method stub
		if(this.soluciones.size() > 0) {
			return this.soluciones.get(0);
		}
		return null;
	}
}
