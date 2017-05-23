package com.trs.gov.kpi.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by linwei on 2017/5/23.
 */
public class SimpleTree<T> {

    private Node<T> root;

    public SimpleTree(T rootData) {
        root = new Node<>(null, rootData);
    }

    public Node<T> getRoot() {
        return root;
    }

    public static class Node<T> {
        private T data;
        private Node<T> parent;
        private List<Node<T>> children;

        public Node(Node<T> parent, T data) {
            this.parent = parent;
            this.data = data;
        }

        public Node<T> addChild(Node<T> child) {
            if (children == null) {
                children = new ArrayList<>();
            }
            children.add(child);
            return child;
        }

        public T getData() {
            return data;
        }

        public Node<T> getParent() {
            return parent;
        }

        public List<Node<T>> getChildren() {
            return children;
        }
    }
}
