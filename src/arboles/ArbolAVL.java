/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arboles;

import java.util.List;

import ProyectoED.ItemNotFoundException;
import ProyectoED.LectordeMedline;

/**
 *
 * @author jorge.reyes
 * @param <T>
 */
public class ArbolAVL<T extends Comparable<T>> {
    protected NodoAVL<T> raiz;

    public ArbolAVL(T o) {
        raiz = new NodoAVL<>(o);
    }

    public void inOrden() {
        if (raiz != null)
            raiz.inOrden();
    }

    public void preOrden() {
        if (raiz != null)
            raiz.preOrden();
    }

    public void posOrden() {
        if (raiz != null)
            raiz.posOrden();
    }

    public void insertar(T o) {
        insertarOrdenado(raiz, o);
    }

    public void insertarOrdenado(NodoAVL<T> n, T o) {
        if (o.compareTo(n.getDato()) < 0) {
            if (n.getIzq() == null) {
                n.setIzq(new NodoAVL<>(o, null, null, n));
                recalcularFE(n);
            } else
                insertarOrdenado((NodoAVL<T>) n.getIzq(), o);
        } else {
            if (o.compareTo(n.getDato()) > 0) {
                if (n.getDer() == null) {
                    n.setDer(new NodoAVL<>(o, null, null, n));
                    recalcularFE(n);
                } else
                    insertarOrdenado((NodoAVL<T>) n.getDer(), o);
            }
        }
    }

    public void recalcularFE(NodoAVL<T> nodo) {
        if (nodo != null) {
            nodo.setFe(NodoAVL.altura((NodoAVL<T>) nodo.getDer()) -
                    NodoAVL.altura((NodoAVL<T>) nodo.getIzq()));
            if (nodo.getFe() == 2 || nodo.getFe() == -2)
                balancear(nodo);
            else
                recalcularFE(nodo.getPadre());
        }
    }

    public void balancear(NodoAVL<T> nodo) {
        int feActual = nodo.getFe();
        if (feActual == 2) {
            switch (((NodoAVL<T>) nodo.getDer()).getFe()) {
                case 0:
                case 1:
                    rotacionDD(nodo);
                    break;
                case -1:
                    rotacionDI(nodo);
                    break;
            }
        } else {
            switch (((NodoAVL<T>) nodo.getIzq()).getFe()) {
                case 0:
                case -1:
                    rotacionII(nodo);
                    break;
                case 1:
                    rotacionID(nodo);
                    break;
            }
        }
    }

    public void rotacionDD(NodoAVL<T> nodo) {
        NodoAVL<T> Padre = nodo.getPadre();
        NodoAVL<T> P = nodo;
        NodoAVL<T> Q = (NodoAVL<T>) P.getDer();
        NodoAVL<T> B = (NodoAVL<T>) Q.getIzq();

        if (Padre != null)
            if (Padre.getDer() == P)
                Padre.setDer(Q);
            else
                Padre.setIzq(Q);
        else
            raiz = Q;

        // Reconstruir el árbol
        P.setDer(B);
        Q.setIzq(P);

        // Reasignación de Padres
        P.setPadre(Q);
        if (B != null)
            B.setPadre(P);
        Q.setPadre(Padre);
        // Ajustar los Factores de Equilibrio
        P.setFe(0);
        Q.setFe(0);
    }

    public void rotacionII(NodoAVL<T> nodo) {
        NodoAVL<T> Padre = nodo.getPadre();
        NodoAVL<T> P = nodo;
        NodoAVL<T> Q = (NodoAVL<T>) P.getIzq();
        NodoAVL<T> B = (NodoAVL<T>) Q.getDer();
        if (Padre != null)
            if (Padre.getIzq() == P)
                Padre.setIzq(Q);
            else
                Padre.setDer(Q);
        else
            raiz = Q;
        // Recontruir el árbol
        P.setIzq(B);
        Q.setDer(P);
        // Reasignar a los papás
        P.setPadre(Q);
        if (B != null)
            B.setPadre(P);
        Q.setPadre(Padre);
        // Ajustar los FE
        P.setFe(0);
        Q.setFe(0);
    }

    public void rotacionID(NodoAVL<T> nodo) {
        NodoAVL<T> Padre = nodo.getPadre();
        NodoAVL<T> P = nodo;
        NodoAVL<T> Q = (NodoAVL<T>) P.getIzq();
        NodoAVL<T> R = (NodoAVL<T>) Q.getDer();
        NodoAVL<T> B = (NodoAVL<T>) R.getIzq();
        NodoAVL<T> C = (NodoAVL<T>) R.getDer();
        if (Padre != null)
            if (Padre.getDer() == nodo)
                Padre.setDer(R);
            else
                Padre.setIzq(R);
        else
            raiz = R;

        // Reconstruir el árbol
        Q.setDer(B);
        P.setIzq(C);
        R.setIzq(Q);
        R.setDer(P);

        // Reasignar los padres
        R.setPadre(Padre);
        P.setPadre(R);
        Q.setPadre(R);
        if (B != null)
            B.setPadre(Q);
        if (C != null)
            C.setPadre(P);

        // Reasignar los FE
        switch (R.getFe()) {
            case -1:
                Q.setFe(0);
                P.setFe(1);
                break;
            case 0:
                Q.setFe(0);
                P.setFe(0);
                break;
            case 1:
                Q.setFe(-1);
                P.setFe(0);
                break;
        }
    }

    public void rotacionDI(NodoAVL<T> nodo) {
        NodoAVL<T> Padre = nodo.getPadre();
        NodoAVL<T> P = nodo;
        NodoAVL<T> Q = (NodoAVL<T>) P.getDer();
        NodoAVL<T> R = (NodoAVL<T>) Q.getIzq();
        NodoAVL<T> B = (NodoAVL<T>) R.getDer();
        NodoAVL<T> C = (NodoAVL<T>) R.getIzq();

        if (Padre != null)
            if (Padre.getIzq() == nodo)
                Padre.setIzq(R);
            else
                Padre.setDer(R);
        else
            raiz = R;

        // Reconstuir el árbol
        Q.setIzq(B);
        P.setDer(C);
        R.setDer(Q);
        R.setIzq(P);

        // Reasignar padres
        R.setPadre(Padre);
        P.setPadre(R);
        Q.setPadre(R);
        if (B != null)
            B.setPadre(Q);
        if (C != null)
            C.setPadre(P);

        // Asignar valores de los FE
        switch (R.getFe()) {
            case -1:
                Q.setFe(0);
                P.setFe(1);
                break;
            case 0:
                Q.setFe(0);
                P.setFe(0);
                break;
            case 1:
                Q.setFe(-1);
                P.setFe(0);
                break;
        }
    }

    public void buscar(T o) {
        buscar(raiz, o);
    }

    public void buscar(NodoBinAVL<T> n, T o) throws ItemNotFoundException {
        if (o.compareTo(n.getDato()) < 0) {
            if (n.getIzq() == null)
                throw new ItemNotFoundException("El elemento no se encuentra");
            else
                buscar(n.getIzq(), o);
        } else if (o.compareTo(n.getDato()) > 0) {
            if (n.getDer() == null)
                throw new ItemNotFoundException("El elemento no se encuentra");
            else
                buscar(n.getDer(), o);
        } else
            System.out.println("El elemento si está en el árbol");
    }

    public static void main(String[] args) {
        // ArbolAVL<Integer> arbol = new ArbolAVL<>(5);
        // arbol.insertar(56);
        // arbol.insertar(8);
        // arbol.insertar(91);
        // arbol.insertar(205);
        // arbol.insertar(512);
        // arbol.insertar(30);
        // arbol.insertar(602);
        // arbol.inOrden();
        List<String> arr = LectordeMedline.readAndProcessFile("medline_CDs.txt");

        long start = System.currentTimeMillis();
        ArbolAVL<String> avl = new ArbolAVL<>(arr.get(0));
        for (int i = 1; i < arr.size(); i++)
            avl.insertar(arr.get(i));
        avl.buscar("encontrado2");
        long finish = System.currentTimeMillis();
        
        System.out.println("Tiempo de ejecución: " + (finish - start) + " ms");

    }
}
