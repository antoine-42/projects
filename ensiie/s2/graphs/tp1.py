# Author: Mike Malecot


from copy import deepcopy

def convertToMatrix(graph) :
    matrix = [[0 for column in range(len(graph))] for row in range(len(graph))]
    #créer un matrice de taille n remplie de 0 correspondant au nombre de noeuds du graphe
    for index in range(len(graph)) :
        for value in graph[index] :
            matrix[index][value] = 1
    return matrix

def royWarshall(matrix) :
    newMatrix = deepcopy(matrix)
    for k in range(len(newMatrix)) :
        for i in range(len(newMatrix)) :
            for j in range(len(newMatrix)):
                newMatrix[i][j] = newMatrix[i][j] or (newMatrix[i][k] and newMatrix[k][j])
    return newMatrix

def royWarshallOnGraph(graph) :
    newGraph = deepcopy(graph)
    for k in range(len(newGraph)):
        for i in range(len(newGraph)) :
            for j in range(len(newGraph)):
                if (j not in newGraph[i] and (k in newGraph[i] and j in newGraph[k])) :
                    newGraph[i].append(j)
    return newGraph

def depthFirstPlus(graph, currentNode = 0, result = [0]) :
    if(len(graph[currentNode]) > 0) :
        for node in graph[currentNode] :
            if(node not in result) :
                result.append(node)
                result = list(set(result + depthFirstPlus(graph, node, result)))
    return result

def depthFirstMinus(graph, currentNode = 0, result = [0]) :
    for nodeIndex in range(len(graph)) :
        if(currentNode in graph[nodeIndex]) :
            if(nodeIndex not in result) :
                result.append(nodeIndex)
                result = list(set(result + depthFirstMinus(graph, nodeIndex, result)))
    return result

def stronglyConnectedComponent(graph,  source = 0, result = [0]) :
    return list( set(depthFirstPlus(graph, source, list(result))) & set(depthFirstMinus(graph, source, list(result))))


if __name__ == "__main__":
    graph = [[1,2],[2],[3],[4],[]]
    print("graph initial", graph) #affiche le graph initial
    m1 = convertToMatrix(graph) #retourne la matrice d'adjacence correspondant au graphe de succésseurs passé en paramètre
    print("matrice d'adjacence", m1) #affiche la matrice d'adjacence
    m2 = royWarshall(m1) #applique royWarshall à la matrice d'adjacence passé en paramètre
    print("matrice de fermetures transitives", m2) #affiche la matrice de fermeture transitive
    m3 = royWarshallOnGraph(graph) #applique royWarshall à la matrice d'adjacence passé en paramètre au graphe de successeurs passé en paramètre
    print("graphe de fermetures transitives", m3) #affiche le graphe de fermeture transitive
    print("propagation des successeurs", depthFirstPlus(graph)) #affiche la propagation des successeurs
    print("")
    graph_pred = [[],[0],[0,1],[2],[3]]
    print("graph_pred : ", graph_pred) #affiche le graph (graph_pred : référence au graph du dernière exercice du TD)
    print("propagation des prédécesseurs sur graph_pred :", depthFirstMinus(graph_pred)) #affiche la propagation des prédécesseurs
    print("")
    graphWithSCC = [[1,2],[2],[3,0],[],[]]
    print("graphWithSCC : ", graphWithSCC) #affiche le graphe qui dispose de composantes fortement connexes
    print("composantes fortements connexes du graphe :", stronglyConnectedComponent(graphWithSCC, 0 , [0])) #affiche les composantes fortement connexes du graphe
