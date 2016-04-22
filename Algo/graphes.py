class GraphNode:
    def __init__(self, value, graph):
        self.links = []
        self.visited = False
        self.value = value
        graph.nodes.append(self)

    def __str__(self):
        return self.value

class GraphArc:
    def __init__(self, nodeA, nodeB):
        self.nodeA = nodeA
        self.nodeB = nodeB

class Graph:
    def __init__(self):
        self.nodes = []

    def add_node(self, val):
        n = GraphNode(val)
        self.nodes.append(n)

    def link_by_val(self, valA, valB):
        nA = None
        nB = None
        for n in self.nodes:
            if n.value == valA:
                nA = n
            elif n.value == valB:
                nB = n
        if nA is not None and nB is not None:
            arc = GraphArc(nA, nB)
            nA.links.append(arc)

    def link_by_val_both_dirs(self, valA, valB):
        self.link_by_val(valA, valB)
        self.link_by_val(valB, valA)

    def parc_prof_rec(self, start_node, result_list, viewed_nodes=[], visited_nodes=[]):
        print("Actual node : "+start_node.value)

        result_list.append(start_node)

        if start_node not in visited_nodes:
            visited_nodes.append(start_node)
        i = 0
        for link in start_node.links:
            if link.nodeB not in viewed_nodes:
                viewed_nodes.insert(i,link.nodeB)
                i += 1
        new_node = viewed_nodes.pop(0)
        while new_node in visited_nodes:
            if len(viewed_nodes) == 0:
                break
            new_node = viewed_nodes.pop(0)
        if new_node not in visited_nodes:
            self.parc_prof_rec(new_node, result_list, viewed_nodes, visited_nodes)




    def list_adj_list(self):
        for n in self.nodes:
            out = str(n)
            out += " -> "
            for l in n.links:
                out += str(l.nodeB) + " "
            print(out)

if __name__ == "__main__":
    g = Graph()

    na = GraphNode('A', g)
    nb = GraphNode('B', g)
    nc = GraphNode('C', g)
    nd = GraphNode('D', g)
    ne = GraphNode('E', g)
    nf = GraphNode('F', g)
    ng = GraphNode('G', g)

    g.link_by_val('A', 'B')
    g.link_by_val('A', 'F')
    g.link_by_val('A', 'G')
    g.link_by_val('A', 'C')
    g.link_by_val('B', 'A')
    g.link_by_val('C', 'A')
    g.link_by_val('D', 'E')
    g.link_by_val('D', 'F')
    g.link_by_val('E', 'G')
    g.link_by_val('E', 'F')
    g.link_by_val('E', 'D')
    g.link_by_val('F', 'A')
    g.link_by_val('F', 'D')
    g.link_by_val('F', 'E')
    g.link_by_val('G', 'A')
    g.link_by_val('G', 'E')

    g.list_adj_list()

    result_list = []

    g.parc_prof_rec(nf, result_list)

    resstr = ""
    for node in result_list:
        resstr += str(node)
        resstr += " "
    print(resstr)
