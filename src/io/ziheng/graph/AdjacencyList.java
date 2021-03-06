package io.ziheng.graph;

import io.ziheng.graph.Graph;
import io.ziheng.graph.lookup.LookUpTable;
import io.ziheng.graph.lookup.LinkedListLookUpTable;
import io.ziheng.graph.lookup.RedBlackTreeSetLookUpTable;

import java.lang.Cloneable;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.HashMap;

public class AdjacencyList implements Graph, Cloneable {

    private int vertexNum;
    private int edgeNum;
    private LookUpTable<Integer>[] adjacencyList;

    private AdjacencyList() {
        // ...
    }

    @SuppressWarnings("unchecked")
    public static AdjacencyList buildWith(int[][] adjMatrix) {
        if (adjMatrix == null ||
            adjMatrix.length == 0 ||
            adjMatrix[0].length != 2 ||
            adjMatrix[0][0] < 0 ||
            adjMatrix[0][1] < 0) {
            return null;
        }
        AdjacencyList adj = new AdjacencyList();
        adj.vertexNum = adjMatrix[0][0];
        adj.edgeNum = adjMatrix[0][1];
        adj.adjacencyList =
            (LookUpTable<Integer>[])new LookUpTable[adj.vertexNum];
        for (int i = 0; i < adj.vertexNum; i++) {
            adj.adjacencyList[i] = new LinkedListLookUpTable<Integer>();
            //adj.adjacencyList[i] = new RedBlackTreeSetLookUpTable<Integer>();
        }
        for (int i = 1; i < adjMatrix.length; i++) {
            int vertexA = adjMatrix[i][0];
            int vertexB = adjMatrix[i][1];
            vaildateVertex(vertexA, adj.vertexNum);
            vaildateVertex(vertexB, adj.vertexNum);
            adj.adjacencyList[vertexA].add(vertexB);
            adj.adjacencyList[vertexB].add(vertexA);
        }
        return adj;
    }
    @SuppressWarnings("unchecked")
    public static AdjacencyList buildWith(String fileName) {
        try {
            AdjacencyList adj = new AdjacencyList();
            File file = new File(fileName);
            Scanner scanner = new Scanner(file);
            adj.vertexNum = scanner.nextInt();
            adj.edgeNum = scanner.nextInt();
            adj.adjacencyList =
                (LookUpTable<Integer>[])new LookUpTable[adj.vertexNum];
            for (int i = 0; i < adj.vertexNum; i++) {
                //adj.adjacencyList[i] = new LinkedListLookUpTable<Integer>();
                adj.adjacencyList[i] = new RedBlackTreeSetLookUpTable<Integer>();
            }
            for (int i = 0; i < adj.edgeNum; i++) {
                int vertexA = scanner.nextInt();
                int vertexB = scanner.nextInt();
                vaildateVertex(vertexA, adj.vertexNum);
                vaildateVertex(vertexB, adj.vertexNum);
                adj.adjacencyList[vertexA].add(vertexB);
                adj.adjacencyList[vertexB].add(vertexA);
            }
            scanner.close();
            return adj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public int getVertexNum() {
        return vertexNum;
    }
    @Override
    public int getEdgeNum() {
        return edgeNum;
    }
    @Override
    public boolean hasEdge(int vertexA, int vertexB) {
        vaildateVertex(vertexA, this.vertexNum);
        vaildateVertex(vertexB, this.vertexNum);
        return adjacencyList[vertexA].contains(vertexB);
    }
    @Override
    public void removeEdge(int vertexA, int vertexB) {
        vaildateVertex(vertexA, vertexNum);
        vaildateVertex(vertexB, vertexNum);
        adjacencyList[vertexA].remove(vertexB);
        adjacencyList[vertexB].remove(vertexA);
    }
    @Override
    public boolean addEdge(int vertexA, int vertexB) {
        vaildateVertex(vertexA, vertexNum);
        vaildateVertex(vertexB, vertexNum);
        if (!hasEdge(vertexA, vertexB)) {
            adjacencyList[vertexA].add(vertexB);
            adjacencyList[vertexB].add(vertexA);
            return true;
        }
        return false;
    }
    @Override
    public void clearEdges() {
        for (int i = 0; i < vertexNum; i++) {
            adjacencyList[i].clear();
        }
    }
    @Override
    public int[] getAdjacentVertex(int vertex) {
        vaildateVertex(vertex, this.vertexNum);
        int[] resultList = new int[adjacencyList[vertex].size()];
        int i = 0;
        for (LookUpTable.Iterator<Integer> iterator = adjacencyList[vertex].iterator();
            iterator.hasNext(); /* ... */ ) {
            resultList[i] = iterator.next();
            i++;
        }
        return resultList;
    }
    @Override
    public int degree(int vertex) {
        vaildateVertex(vertex, this.vertexNum);
        return adjacencyList[vertex].size();
    }
    @Override
    public boolean hasCycle() {
        Map<String, Boolean> map = new HashMap<>();
        map.put("hasCycleState", false);
        boolean[] visited = new boolean[vertexNum];
        for (int i = 0; i < vertexNum; i++) {
            visited[i] = false;
        }
        for (int i = 0; i < vertexNum; i++) {
            if (!visited[i]) {
                hasCycle0(i, i, visited, map);
            }
        }
        return map.get("hasCycleState");
    }
    private void hasCycle0(
        int currentVertex, int parentVertex, boolean[] visited, Map<String, Boolean> map) {
        visited[currentVertex] = true;
        for (int adjVertex : getAdjacentVertex(currentVertex)) {
            if (!visited[adjVertex]) {
                hasCycle0(adjVertex, currentVertex, visited, map);
            } else {
                if (adjVertex != parentVertex) {
                    map.replace("hasCycleState", true);
                }
            }
        }
    }
    @Override
    public int connectedComponents() {
        Boolean[] visited = new Boolean[vertexNum];
        setArray(visited, false);
        int connectedComponentsNum = 0;
        for (int i = 0; i < vertexNum; i++) {
            if (!visited[i]) {
                connectedComponents0(i, visited);
                connectedComponentsNum++;
            }
        }
        return connectedComponentsNum;
    }
    private void connectedComponents0(int vertex, Boolean[] visited) {
        visited[vertex] = true;
        for (int adjVertex : getAdjacentVertex(vertex)) {
            if (!visited[adjVertex]) {
                connectedComponents0(adjVertex, visited);
            }
        }
    }
    @Override
    public int[] findShortestPath(int sourceVertex, int destinationVertex) {
        vaildateVertex(sourceVertex, this.vertexNum);
        vaildateVertex(destinationVertex, this.vertexNum);
        List<Integer> resultList = new LinkedList<Integer>();
        Integer[] visited = new Integer[vertexNum];
        setArray(visited, -1);
        findShortestPath0(
            sourceVertex,
            destinationVertex,
            sourceVertex,
            visited
        );
        //System.out.println(Arrays.toString(visited));
        if (visited[destinationVertex] != -1) {
            for (int currentVertex = destinationVertex;
                currentVertex != sourceVertex;
                currentVertex = visited[currentVertex]) {
                resultList.add(currentVertex);
            }
            resultList.add(sourceVertex);
            Collections.reverse(resultList);
        }
        return toIntArray(resultList);
    }
    private void findShortestPath0(
        int sourceVertex,
        int destinationVertex,
        int parentVertex,
        Integer[] visited) {
        Queue<Integer> queue = new LinkedList<>();
        queue.offer(sourceVertex);
        visited[sourceVertex] = parentVertex;
        while (!queue.isEmpty()) {
            int currentVertex = queue.poll();
            for (int adjVertex : getAdjacentVertex(currentVertex)) {
                if (visited[adjVertex] == -1) {
                    visited[adjVertex] = currentVertex;
                    if (adjVertex == destinationVertex) {
                        return;
                    }
                    queue.offer(adjVertex);
                }
            }
        }
    }
    @Override
    public int findShortestPathLength(int sourceVertex, int destinationVertex) {
        return findShortestPath(sourceVertex, destinationVertex).length - 1;
    }
    @Override
    public int[] findAPath(int sourceVertex, int destinationVertex) {
        vaildateVertex(sourceVertex, this.vertexNum);
        vaildateVertex(destinationVertex, this.vertexNum);
        List<Integer> resultList = new LinkedList<Integer>();
        int[] visited = new int[vertexNum];
        for (int i = 0; i < vertexNum; i++) {
            visited[i] = -1;
        }
        findAPath0(sourceVertex, destinationVertex, sourceVertex, visited);
        //System.out.println(Arrays.toString(visited));
        if (visited[destinationVertex] != -1) {
            for (int currentVertex = destinationVertex;
                currentVertex != sourceVertex;
                currentVertex = visited[currentVertex]) {
                resultList.add(currentVertex);
            }
            resultList.add(sourceVertex);
            Collections.reverse(resultList);
        }
        return toIntArray(resultList);
    }
    private void findAPath0(
        int currentVertex, int destinationVertex, int parentVertex, int[] visited) {
        visited[currentVertex] = parentVertex;
        if (currentVertex == destinationVertex) {
            return;
        }
        for (int adjVertex : getAdjacentVertex(currentVertex)) {
            if (visited[adjVertex] == -1) {
                findAPath0(adjVertex, destinationVertex, currentVertex, visited);
            }
        }
    }
    @Override
    public boolean isConnected(int vertexA, int vertexB) {
        vaildateVertex(vertexA, this.vertexNum);
        vaildateVertex(vertexB, this.vertexNum);
        List<Integer> resultList = new LinkedList<Integer>();
        Boolean[] visited = new Boolean[vertexNum];
        setArray(visited, false);
        depthFirstSearch0(vertexA, visited, resultList);
        return resultList.contains(vertexB);
    }
    @Override
    public int[] depthFirstSearch() {
        List<Integer> resultList = new LinkedList<Integer>();
        Boolean[] visited = new Boolean[vertexNum];
        setArray(visited, false);
        for (int i = 0; i < vertexNum; i++) {
            if (!visited[i]) {
                //depthFirstSearch0(i, visited, resultList);
                depthFirstSearch1(i, visited, resultList);
            }
        }
        return toIntArray(resultList);
    }
    private void depthFirstSearch0(
        int vertex, Boolean[] visited, List<Integer> resultList) {
        visited[vertex] = true;
        resultList.add(vertex);
        for (int v : getAdjacentVertex(vertex)) {
            if (!visited[v]) {
                depthFirstSearch0(v, visited, resultList);
            }
        }
    }
    private void depthFirstSearch1(
        int vertex, Boolean[] visited, List<Integer> resultList) {
        Deque<Integer> stack = new LinkedList<>();
        stack.push(vertex);
        visited[vertex] = true;
        resultList.add(vertex);
        while (!stack.isEmpty()) {
            int currentVertex = stack.pop();
            for (int adjVertex : getAdjacentVertex(currentVertex)) {
                if (!visited[adjVertex]) {
                    stack.push(adjVertex);
                    visited[adjVertex] = true;
                    resultList.add(adjVertex);
                }
            }
        }
    }
    @Override
    public int[] breadthFirstSearch() {
        List<Integer> resultList = new LinkedList<Integer>();
        Boolean[] visited = new Boolean[vertexNum];
        setArray(visited, false);
        for (int i = 0; i < vertexNum; i++) {
            if (!visited[i]) {
                breadthFirstSearch0(i, visited, resultList);
            }
        }
        return toIntArray(resultList);
    }
    private void breadthFirstSearch0(
        int vertex, Boolean[] visited, List<Integer> resultList) {
        Queue<Integer> queue = new LinkedList<>();
        queue.offer(vertex);
        visited[vertex] = true;
        while (!queue.isEmpty()) {
            int currentVertex = queue.poll();
            resultList.add(currentVertex);
            for (int adjVertex : getAdjacentVertex(currentVertex)) {
                if (!visited[adjVertex]) {
                    queue.offer(adjVertex);
                    visited[adjVertex] = true;
                }
            }
        }
    }
    private int hamiltonianPathEnd = -1;
    @Override
    public int[] hamiltonianCircuit() {
        Integer[] visited = new Integer[vertexNum];
        setArray(visited, -1);
        List<Integer> resultList = new ArrayList<>();
        // 从顶点 0 开始搜寻哈密尔顿回路
        if (hamiltonianCircuit0(0, 0, visited)) {
            int currentVertex = hamiltonianPathEnd;
            while (currentVertex != 0) {
                resultList.add(currentVertex);
                currentVertex = visited[currentVertex];
            }
            resultList.add(0);
            Collections.reverse(resultList);
        }
        return toIntArray(resultList);
    }
    private boolean hamiltonianCircuit0(
        int currentVertex, int parentVertex, Integer[] visited) {
        visited[currentVertex] = parentVertex;
        for (int adjVertex : getAdjacentVertex(currentVertex)) {
            if (visited[adjVertex] == -1) {
                if (hamiltonianCircuit0(adjVertex, currentVertex, visited)) {
                    return true;
                }
            } else {
                if (adjVertex == 0 && allVisited(visited)) {
                    hamiltonianPathEnd = currentVertex;
                    return true;
                }
            }
        }
        // 重置访问状态
        visited[currentVertex] = -1;
        return false;
    }
    @Override
    public int[] hamiltonianPath(int vertex) {
        vaildateVertex(vertex, vertexNum);
        Integer[] visited = new Integer[vertexNum];
        setArray(visited, -1);
        List<Integer> resultList = new ArrayList<>();
        // 从顶点 v 开始搜寻哈密尔顿路径
        if (hamiltonianPath0(vertex, vertex, visited)) {
            int currentVertex = hamiltonianPathEnd;
            while (currentVertex != vertex) {
                resultList.add(currentVertex);
                currentVertex = visited[currentVertex];
            }
            resultList.add(vertex);
            Collections.reverse(resultList);
        }
        return toIntArray(resultList);
    }
    private boolean hamiltonianPath0(
        int currentVertex, int parentVertex, Integer[] visited) {
        visited[currentVertex] = parentVertex;
        for (int adjVertex : getAdjacentVertex(currentVertex)) {
            if (visited[adjVertex] == -1) {
                if (hamiltonianPath0(adjVertex, currentVertex, visited)) {
                    return true;
                }
            } else {
                if (allVisited(visited)) {
                    hamiltonianPathEnd = currentVertex;
                    return true;
                }
            }
        }
        // 重置访问状态
        visited[currentVertex] = -1;
        return false;
    }
    private boolean allVisited(Integer[] visited) {
        for (int i = 0; i < vertexNum; i++) {
            if (visited[i] == -1) {
                return false;
            }
        }
        return true;
    }
    private static void vaildateVertex(int vertex, int vertexNum) {
        if (vertex < 0 || vertex >= vertexNum) {
            throw new IllegalArgumentException(
                "[ERROR]: vertex " + vertex + " is invalid!"
            );
        }
    }
    private static <T> void setArray(T[] arr, T val) {
        for (int i = 0; i < arr.length; i++) {
            arr[i] = val;
        }
    }
    private int[] toIntArray(List<Integer> list) {
        int[] resultArray = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            resultArray[i] = list.get(i);
        }
        return resultArray;
    }
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.format(
            "vertexNum = %d, edgeNum = %d",
            this.vertexNum, this.edgeNum
        ));
        stringBuilder.append(System.lineSeparator());
        for (int i = 0; i < this.vertexNum; i++) {
            stringBuilder.append(String.format("%d: ", i));
            for (LookUpTable.Iterator<Integer> iterator = adjacencyList[i].iterator();
                iterator.hasNext(); /* ... */ ) {
                stringBuilder.append(String.format(
                    "%d ", iterator.next()
                ));
            }
            stringBuilder.append(System.lineSeparator());
        }
        return stringBuilder.toString();
    }
    @SuppressWarnings("unchecked")
    @Override
    public Object clone() throws CloneNotSupportedException {
        AdjacencyList adj = new AdjacencyList();
        adj.vertexNum = this.vertexNum;
        adj.edgeNum = this.edgeNum;
        LookUpTable<Integer>[] adjacencyListClone =
            (LookUpTable<Integer>[])new LookUpTable[adj.vertexNum];
        for (int i = 0; i < vertexNum; i++) {
            adjacencyListClone[i] = new RedBlackTreeSetLookUpTable<>();
            for (LookUpTable.Iterator<Integer> iterator =
                    adjacencyList[i].iterator();
                iterator.hasNext(); /* ... */ ) {
                adjacencyListClone[i].add(iterator.next());
            }
        }
        adj.adjacencyList = adjacencyListClone;
        return adj;
    }
}
/* EOF */