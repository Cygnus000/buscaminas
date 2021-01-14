//javac -encoding "utf-8" buscaminas.java
/**
*Este programa crea y resuelve a traves de la implementacion de dos algoritmos
* un tablero de buscaminas basado en el clasico de windows, tiene 4 modalidades: <br>
*<strong>Principiante</strong> - tablero 9x9 con 10 minas, <br>
*<strong>Intermedio</strong> - tablero 16x16 con 40 minas, <br>
*<strong>Experto</strong> - tablero 16x30 con 99 minas, <br>
*<strong>Personalizado</strong> - desde 9x9 con 10 minas. <br>
*Los algoritmos usados son: <br>
*<strong>Tirada aleatoria</strong>: destapa una casilla al azar, <br>
*<strong>Basico</strong>: Marca las casillas que se infiere son minas
* a traves de las 8 casillas con numero a su alrededor o destapa las casillas que se sabe no pueden ser minas<br>
*<strong>Avanzado</strong> (en progreso): Delimita fronteras de áreas rodeadas de casillas con numeros y 
* calcula las configuraciones que satisfacen la resolucion
* del juego y asi destapar casillas improbables de tener una mina o marcar
* casillas cuya unica posibilidad es tener una mina. 
*
*@author NowHereMan
*/
public class buscaminas
{
    boolean juego;          //Indica FALSE si el juego a terminado o TRUE si aun no se gana o pierde.
    int minasEncontradas;   //Cantidad de minas marcadas con M.
    int minasExistentes;    //Cantidad total de minas exixtentes en el tablero.
    int casillasDestapadas; //Cantidad de casillas destapadas o Marcadas como minas (M) en el juego.
    int casillasExistentes; //Cantidad total de casillas existentes del tablero.
    byte oculto[][];         //Tablero de referencia, contiene donde estan las minas y el numero de cada casilla, solo es visible al final y se usa al destapar una casilla.
    byte tablero[][];        //El tablero que ve el jugador, es donde se visualiza el estado del juego.

   public static void main(String Arg[])
   {
     if(Arg.length==0) //Ejecucion tipica del programa sin argumentos.
     {
         
       java.util.Scanner tec = new java.util.Scanner(System.in);
       System.out.print("              tablero   minas\nPrincipiante    9x9      10    [1] \nIntermedio     16x16     40    [2] \nExperto        16x30     99    [3] \nPersonalizado                  [4]\nDificultad: ");
       switch(tec.nextInt())
        {
         default :
         {
           buscaminas b=new buscaminas(9,9,10);
           b.iniciarJuego();
           break;
         }
         case 1 :  //Inicia el juego en nivel principiante (tablero de 9x9 con 10 minas).
         {
           buscaminas b=new buscaminas(9,9,10);
           b.iniciarJuego();
           break;
         }
         case 2 :  //Inicia el juego en nivel intermedio (tablero de 16x16 con 40 minas).
         {
           buscaminas b=new buscaminas(16,16,40);
           b.iniciarJuego();
           break;
         }
         case 3 :  //Inicia el juego en nivel experto (tablero de 16x30 con 99 minas).
         {
           buscaminas b=new buscaminas(16,30,99);
           b.iniciarJuego();
           break;
         }
         case 4 :  //Inicia el juego en nivel personalizado.
         {
           System.out.println("¿De qué tamaño será el tablero?");
           int ancho=tec.nextInt();
           int alto=tec.nextInt();
           System.out.println("¿Cuantas minas contendrá tablero?");
           int numMinas=tec.nextInt();
           buscaminas b=new buscaminas(ancho,alto,numMinas);
           b.iniciarJuego();
           break; 
        }          
      }
     }
     else if(Arg.length==1) //Un solo argumento (para que el programa funcione debe ser entero).
     {
            switch(Integer.parseInt(Arg[0]))
      {
        default :
        {
           buscaminas b=new buscaminas(9,9,10);
           b.iniciarJuego();
           break;
        }
        case 1 :  //Inicia el juego en nivel principiante (tablero de 9x9 con 10 minas).
        {
           buscaminas b=new buscaminas(9,9,10);
           b.iniciarJuego();
           break;
        }
        case 2 :  //Inicia el juego en nivel intermedio (tablero de 16x16 con 40 minas).
        {
           buscaminas b=new buscaminas(16,16,40);
           b.iniciarJuego();
           break;
        }
        case 3 :  //Inicia el juego en nivel experto (tablero de 16x30 con 99 minas).
        {
           buscaminas b=new buscaminas(16,30,99);
           b.iniciarJuego();
           break;
        }        
      }
     }
     else if(Arg.length==3) //Se inicia en personalizado transformando a enteros los argumentos, el primero sera el ancho del tablero, el segundo sera el
     {                 //alto y el tercero el numero de minas.
       buscaminas b=new buscaminas(Integer.parseInt(Arg[0]),Integer.parseInt(Arg[1]),Integer.parseInt(Arg[2]));
       b.iniciarJuego();
     }
   }
/**
*Constructor por defecto, crea el objeto con campos por defecto. 
*/   
   public buscaminas(){;}
/**
*Constructor para definir el tamaño del tablero (mín 9x9) y la cantidad de minas (mín 10 minas),
* usa las primeras dos entradas para determinar
* el tamaño del tablero y la ultima para la cantidad de minas. 
*
*@param alto alto del tablero.
*@param ancho ancho del tablero.
*@param numMinas numero de minas.
*/   
   public buscaminas(int ancho, int alto, int numMinas)
   {
    if(ancho<9) ancho=9; if(alto<9) alto=9;
    System.out.println("El tablero será de tamaño: "+ancho+"x"+alto);
    this.casillasExistentes=ancho*alto;
    this.minasExistentes=numMinas;
    if(this.minasExistentes>((int)(this.casillasExistentes*0.8)))  //se llena al máximo el 80% del tablero con minas.
       this.minasExistentes=(int)(this.casillasExistentes*0.8);
    else if(this.minasExistentes<10)
       this.minasExistentes=10;
    System.out.println("El tablero contendrá "+this.minasExistentes+" minas");
    this.oculto=new byte[ancho][alto]; this.tablero=new byte[ancho][alto];
    llenar(this.oculto,this.minasExistentes); // LLenado del tablero oculto para preparar el juego.
    this.juego=true;
   }
/**
*Metodo que recibe un "buscaminas" para resolverlo por medio de dos algoritmos,
* al finalizar la rutina indica si se gano o perdio, la cantidad de minas encontradas,
* asi como la cantidad de casillas destapadas.
*La primera casilla destapada nunca es una mina. 
*/  
   public void iniciarJuego()
   {
    this.PrimeraTirada(); //La primera casilla destapada nunca es mina.
    this.basico();
    while(this.juego) // Mientras no se destape una mina o aun no se marquen todas las minas se sigue jugando.
    {
     this.tirarAleatoriamente();
     if(this.juego)
       this.basico();
//     if(this.juego)
//       this.avanzado();
    }
	 if( !(this.casillasDestapadas==this.casillasExistentes && this.minasExistentes==this.minasEncontradas) )
		 System.out.println("Perdiste :C");
	 else
		 System.out.println("Ganaste! :D");
     System.out.println("Se encontraron "+this.minasEncontradas+" de "+this.minasExistentes+" minas.");
     System.out.println("Se destaparon "+this.casillasDestapadas+" de "+this.casillasExistentes+" casillas.");
     System.out.println("Tablero oculto:");         
     imprimirJuegoActual(this.oculto); // Se imprime el tablero oculto para compararlo con el tablero final del jugador.
   }
   /**
   *Consiste de dos ideas basicas: marcar minas y destapar casillas seguras alrededor de las minas, funciona de manera recursiva
   *asi que se llama a si mismo para revisar el tablero luego de cada modificacion que se hace al destapar o marcar una casilla. 
   */
    public void basico()
   {
       for(int a=0;a<this.tablero.length;a++) //revisa alrededor de (c,d) sin salirse del tablero.
         for(int b=0;b<this.tablero[0].length;b++)
          {   
           if(this.tablero[a][b]>0 && buscar(a,b,this.tablero,0)>0) //si la casilla no es cero y no esta todo destapado alrededor.
           {
               if((this.tablero[a][b]-buscar(a,b,this.tablero,-2))==buscar(a,b,this.tablero,0)) //Si el numero de la casilla observada menos el numero de banderas alrededor es igual al numero de casillas tapadas a su alrededor entonces destapa todas las casillas a su alrededor.
               {
                 for(int c=max(0,a-1);c<min(a+2,this.tablero.length);c++) //revisa alrededor de (c,d) sin salirse del tablero.
                   for(int d=max(0,b-1);d<min(b+2,this.tablero[0].length);d++)
                     if(this.tablero[c][d]==0)
                     {
                       System.out.println("Mina encontrada en ["+(d+1)+","+(c+1)+"] desde: ["+(b+1)+","+(a+1)+"]");
                       this.tablero[c][d]=-2; // Marca la mina en el tablero del juego.
                       this.minasEncontradas++;
                       this.casillasDestapadas++;
                       imprimirJuegoActual(this.tablero);
                     }
                 if(this.casillasDestapadas==this.casillasExistentes)
                 {
                     this.terminarJuego();
                     return;
                 }
                 this.basico(); //Se llama a si mismo luego de modificar el tablero.
                 return;
               }
               if(this.tablero[a][b]==buscar(a,b,this.tablero,-2)) //Si una casilla tiene una cantidad de minas adyacentes igual a su numero destapa todas las casillas adyacentes.
                {
                 for(int c=max(0,a-1);c<min(a+2,this.tablero.length);c++) //revisa alrededor de (c,d) sin salirse del tablero.
                   for(int d=max(0,b-1);d<min(b+2,this.tablero[0].length);d++)
                     if(this.tablero[c][d]==0)
                     {
                         this.destapar(c,d);
                     }
                 System.out.println("Se destapara alrededor de ["+(b+1)+","+(a+1)+"]");
                 imprimirJuegoActual(this.tablero);
                if(this.casillasDestapadas==this.casillasExistentes)
                 {
                     this.terminarJuego();
                     return;
                 }
                 this.basico(); //Se llama a si mismo luego de modificar el tablero.
                 return; 
                }
           }
          }
   }
/**
*En progreso...
*/
  public void avanzado()
  {
     ;
  }
  /**
  *Este metodo devuelve la cantidad de casillas en el tablero marcadas con el numero "numBuscado" alrededor
  *de la casilla en las coordenadas (x,y). 
  *
  *@param x Coordenada x en el tablero.
  *@param y Coordenada y en el tablero.
  *@param T[][] matriz de dos dimensiones.
  *@param numBuscado Numero que se buscara alrededor de la casilla (x,y).
  *@return encontrados Numero que indica la cantidad de veces que se encontro el numero numBuscado alrededor de la casilla (x,y).
  */
  public static byte buscar(int x,int y, byte T[][],int numBuscado)
  {                                               // 
    byte encontrados=0;
    for(int a=max(0,x-1);a<min(x+2,T.length);a++)
      for(int b=max(0,y-1);b<min(y+2,T[0].length);b++)
        if(T[a][b]==numBuscado)
          if(!(x==a && y==b))
             encontrados++;
      return encontrados;
  }
/**
*Devuelve el maximo de dos valores. 
*
*@param a primer valor a compararlo.
*@param segundo valor a comparar.
*/
  public static int max(int a, int b) // Devuelve el maximo de dos numeros.
  {return a>b ? a : b;}
/**
*Devuelve el minimo de dos valores. 
*
*@param a primer valor a compararlo.
*@param segundo valor a comparar.
*/  
  public static int min(int a, int b) // Devuelve el minimo de dos numeros.
  {return a<b ? a : b;}
/**
*Este metodo genera dos numeros aleatorios menores o iguales que el ancho y ancho del tablero para destapar una casilla al azar siempre y cuando no sea una mina. 
*/
  public void PrimeraTirada()
  {
    int x = (int)(Math.random()*this.tablero.length);
    int y = (int)(Math.random()*this.tablero[0].length);
    if(this.tablero[x][y]==0 && this.oculto[x][y]!=9)
    {
    System.out.println("Tirada aleatoria en ["+(y+1)+","+(x+1)+"]");
    this.destapar(x,y);
    imprimirJuegoActual(this.tablero);
    }
    else
        this.PrimeraTirada();
  }
/**
*Este metodo genera dos numeros aleatorios menores o iguales que el ancho y ancho del tablero para destapar una casilla al azar. 
*/
  public void tirarAleatoriamente()
  {
    int x = (int)(Math.random()*this.tablero.length);
    int y = (int)(Math.random()*this.tablero[0].length);
    if(this.tablero[x][y]==0)
    {
    System.out.println("Tirada aleatoria en ["+(y+1)+","+(x+1)+"]");
    this.destapar(x,y);
    imprimirJuegoActual(this.tablero);
    }
  }
/**
*Este metodo recibe una matriz Z de bytes de dos dimensiones que llena con m minas colocandolas de manera
* aleatoria y sumando 1 a las casillas que no sean minas a su alrededor, funciona de manera recursiva
* para garantizar m minas. 
*
*@param Z[][] matriz de enteros dos dimensiones que fungira como el tablero con informacion para el buscaminas.
*@param minas numero positivo que indica la cantidad de minas que contendra el tablero.
*/
  public static void llenar(byte Z[][],int minas)
  {
    if(minas>0)
    {
     int x = (int)(Math.random()*Z.length);
     int y = (int)(Math.random()*Z[0].length);
     if(Z[x][y]!=9)
      {
      Z[x][y]=9;
       for(int a=max(0,x-1);a<min(x+2,Z.length);a++)
        for(int b=max(0,y-1);b<min(y+2,Z[0].length);b++)
         if(Z[a][b]!=9)
          Z[a][b]++;
      }
     else
      {
       llenar(Z,minas);  // En caso de una elegir una casilla que ya tiene mina, se tira de nuevo para tener m minas y no menos.
       return;
      }
      llenar(Z,minas-1);
    }
  }
/**
*Imprime un matriz de enteros modificando la salida para aparentar un tablero de buscaminas.
* [-2] -> [M] (Bandera de mina encontrada),[-1] -> [░]casilla en blanco, [0] -> [▓] casilla sin destapar,
* numero natural n tal que -1 < n < 9 se queda igual, [9] -> [*] Mina. 
*
*@param Z[][] matriz de enteros de dos dimensiones.
*/
  public static void imprimirJuegoActual(byte Z[][])
  {
   for(byte linea[] : Z)
   {
       System.out.print("   ");
       for(byte valor : linea)
       {
        switch(valor)
          {
            case -1:
            {
               System.out.print("░"+" ");
               break;
            }
            case 9:
            {
                System.out.print("*"+" ");
                break;
            }
            case -2:
            {
                System.out.print("M"+" ");
                break;
            }
            case 0:
            {
                System.out.print("▓"+" ");
                break;
            }
            default:
            {
                System.out.print(valor+" ");
                break;
            }
          }
       }
       System.out.println();
    }
    System.out.println("");
	pausa();
  }
/**
*Dada una casilla (x,y) se destapa en el tablero del juego copiando el valor del tablero oculto 
*si la casilla descubierta tiene una mina (9) el juego termina. <br>
* Si el tablero oculto indica una casilla vacia entonces destapa todo alrededor. <br>
* Es recursivo para destapar casillas vacias y todo a su alrededor. <br> 
* Si intenta destapar una casilla marcada como mina no hace nada.
*
*@param x coordenada x.
*@param y coordenada y.
*/
  public void destapar(int x, int y)
  {
     if(this.oculto[x][y]==0)
      {
       if(this.tablero[x][y]!=-1) // Si la casilla no se ha destapado antes entonces la destapa.
       {
        this.casillasDestapadas++; 
        this.tablero[x][y]=-1;
       }
       for(int a=max(0,x-1);a<min(x+2,this.tablero.length);a++)
         for(int b=max(0,y-1);b<min(y+2,this.tablero[0].length);b++)
           if(this.tablero[a][b]!=-1)
             destapar(a,b);
      }
      else
       {
         if(this.tablero[x][y]==-2)
         {
           return;
         }
         if(this.tablero[x][y]==0&&this.oculto[x][y]!=9) // Si la casilla esta tapada en el tablero y no es una mina entonces coloca el numero correspondiente.
         {
           this.casillasDestapadas++;
           this.tablero[x][y]=this.oculto[x][y];
         }
         if(this.oculto[x][y]==9)
         {
           this.tablero[x][y]=this.oculto[x][y];
           this.casillasDestapadas++;
           this.minasEncontradas++;
           this.terminarJuego();
         }
       }
  }
/**
*Este metodo pasa el valor del juego a FALSE marcando su fin.
*/
  public void terminarJuego()
  {
      this.juego=false;
  }
  static public void pausa()
   { 
          String seguir;
          java.util.Scanner teclado = new java.util.Scanner(System.in);
          System.out.println("Presione enter para continuar...");
          try
            {
             seguir = teclado.nextLine();
            }   
         catch(Exception e)
          {}  
     }
/**
*Este metodo pausa la ejecucion hasta que se presione la tecla Enter (intro, return).
*/
}