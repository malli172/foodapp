package com.project.foodapp.model;

import jakarta.persistence.*;


import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "search_engine")
public class SearchEngine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "search_engine_id")
    private List<Node> nodes;

    public SearchEngine() {
        this.nodes = new ArrayList<>();
    }

    public void add(String word, String restaurantId) {
        Node node = new Node(word, restaurantId);
        nodes.add(node);
    }

    public List<String> search(String word) {
        List<String> restaurantIds = new ArrayList<>();
        for (Node node : nodes) {
            if (node.getWord().equalsIgnoreCase(word)) {
                restaurantIds.add(node.getRestaurantId());
            }
        }
        return restaurantIds;
    }

    public void remove(String word, String restaurantId) {
        nodes.removeIf(node -> node.getWord().equalsIgnoreCase(word) && node.getRestaurantId().equals(restaurantId));
    }

    // Other getters, setters, and methods

    @Entity
    @Table(name = "node")
    public static class Node {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String word;
        private String restaurantId;

        public Node() {
        }

        public Node(String word, String restaurantId) {
            this.word = word;
            this.restaurantId = restaurantId;
        }

        // Getters and setters

        public String getWord() {
            return word;
        }

        public void setWord(String word) {
            this.word = word;
        }

        public String getRestaurantId() {
            return restaurantId;
        }

        public void setRestaurantId(String restaurantId) {
            this.restaurantId = restaurantId;
        }
    }
}

