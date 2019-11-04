# 二叉树遍历

二叉树的遍历是指：**按照某条搜索路径依次访问二叉树中的每个结点，使得二叉树中的每个结点均被访问一次，且仅被访问一次。**

由于二叉树是递归定义的，遍历一棵二叉树需要决定对其根节点、左右子树的先后访问顺序，由此诞生几种不同的遍历次序：

- 前序遍历（根左右）

- 中序遍历（左根右）

- 后序遍历（左右根）

其中「序」指的是根结点的访问次序。

```plain
     1
   /   \
  2     3
 / \   / \
4   5 6   7
```
> 注：二叉树实例

# 二叉树前序遍历（Preorder Traversal）

![PreOrderTraversal][PreOrderTraversal]

> 图：二叉树前序遍历示意图

# 二叉树中序遍历（Inorder Traversal）

![InOrderTraversal][InOrderTraversal]

> 图：二叉树中序遍历示意图

# 二叉树后序遍历（Postorder Traversal）

![PostOrderTraversal][PostOrderTraversal]

> 图：二叉树后序遍历示意图

# 二叉树层序（遍历（Level Order Traversal）

![LevelOrderTraversal][LevelOrderTraversal]

> 图：二叉树层序遍历示意图

```java

```
> 代码清单：二叉树层序遍历（Level Order Traversal）


[PreOrderTraversal]: ../../images/DataStructuresAndAlgorithms-BinaryTreeTraversal-N-PreOrderTraversal.png

[InOrderTraversal]: ../../images/DataStructuresAndAlgorithms-BinaryTreeTraversal-N-InOrderTraversal.png

[PostOrderTraversal]: ../../images/DataStructuresAndAlgorithms-BinaryTreeTraversal-N-PostOrderTraversal.png

[LevelOrderTraversal]: ../../images/DataStructuresAndAlgorithms-BinaryTreeTraversal-N-LevelOrderTraversal.png

<!-- EOF -->