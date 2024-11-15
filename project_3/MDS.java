package project_3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Multi-dimensional search implementation for a product database system.
 * Supports operations for inserting, finding, and deleting items based on their ID,
 * as well as searching items by their descriptive attributes and price ranges.
 * Each item has three attributes: id (int), description (list of ints), and price (int).
 *
 * @author Giridhar Nair
 */
public class MDS {

    /**
     * Inner class representing an item in the database.
     * Contains the item's ID, price, and list of descriptive integers.
     */
    private class Item implements Comparable<Item> {

        int id;
        int price;
        List<Integer> description;

        /**
         * Constructs a new item with the given attributes.
         * @param id The unique identifier of the item
         * @param price The price of the item
         * @param description List of integers describing the item
         */
        public Item(int id, int price, List<Integer> description) {
            this.id = id;
            this.price = price;
            this.description = description;
        }

        /**
         * Compares this item with another item based on their IDs.
         * @param other The item to compare with
         * @return A negative integer if this item's ID is less than the other's,
         *         zero if they're equal, or a positive integer if greater
         */
        @Override
        public int compareTo(Item other) {
            return Integer.compare(this.id, other.id);
        }
    }

    TreeMap<Integer, Item> itemMap;
    HashMap<Integer, TreeSet<Item>> descriptionMap;

    /**
     * Constructs an empty MDS with initialized data structures.
     */
    public MDS() {
        itemMap = new TreeMap<>();
        descriptionMap = new HashMap<>();
    }

    /**
     * Helper method to remove an item's descriptions from the description map.
     * @param item The item to remove
     */
    private void removeFromDescriptionMap(Item item) {
        if (item.description != null) {
            for (int description : item.description) {
                TreeSet<Item> items = descriptionMap.get(description);
                if (items != null) {
                    items.remove(item);
                    if (items.isEmpty()) {
                        descriptionMap.remove(description);
                    }
                }
            }
        }
    }

    /**
     * Inserts a new item or updates an existing item's information.
     * @param id The unique identifier of the item
     * @param price The price of the item
     * @param list The list of descriptive integers for the item
     * @return 1 if the item is new, 0 if an existing item was updated
     */
    public int insert(int id, int price, List<Integer> list) {
        boolean isNewItem = !itemMap.containsKey(id);

        if (!isNewItem) {
            removeFromDescriptionMap(itemMap.get(id));
        }

        Item newItem = new Item(id, price, new LinkedList<>(list));
        itemMap.put(id, newItem);

        if (list != null && !list.isEmpty()) {
            for (int description : list) {
                descriptionMap.computeIfAbsent(description, k -> new TreeSet<>()).add(newItem);
            }
        }

        return isNewItem ? 1 : 0;
    }

    /**
     * Finds the price of an item with the given ID.
     * @param id The unique identifier of the item to find
     * @return The price of the item, or 0 if not found
     */
    public int find(int id) {
        return itemMap.containsKey(id) ? itemMap.get(id).price : 0;
    }

    /**
     * Deletes an item from the database.
     * @param id The unique identifier of the item to delete
     * @return Sum of all integers in the item's description, or 0 if item not found
     */
    public int delete(int id) {
        Item item = itemMap.remove(id);
        if (item == null || item.description == null) {
            return 0;
        }

        removeFromDescriptionMap(item);

        int sum = 0;
        for (int description : item.description) {
            sum += description;
        }

        return sum;
    }

    /**
     * Finds the lowest price among items containing a specific descriptor.
     * @param n The descriptor to search for
     * @return The lowest price among matching items, or 0 if no matches found
     */
    public int findMinPrice(int n) {
        TreeSet<Item> items = descriptionMap.get(n);
        if (items == null) {
            return 0;
        }

        int minPrice = Integer.MAX_VALUE;
        for (Item item : items) {
            minPrice = Math.min(minPrice, item.price);
        }

        return minPrice == Integer.MAX_VALUE ? 0 : minPrice;
    }

    /**
     * Finds the highest price among items containing a specific descriptor.
     * @param n The descriptor to search for
     * @return The highest price among matching items, or 0 if no matches found
     */

    public int findMaxPrice(int n) {
        TreeSet<Item> items = descriptionMap.get(n);
        if (items == null) {
            return 0;
        }

        int maxPrice = Integer.MIN_VALUE;
        for (Item item : items) {
            maxPrice = Math.max(maxPrice, item.price);
        }

        return maxPrice == Integer.MIN_VALUE ? 0 : maxPrice;
    }

    /**
     * Counts items within a price range that contain a specific descriptor.
     * @param n The descriptor to search for
     * @param low The lower bound of the price range (inclusive)
     * @param high The upper bound of the price range (inclusive)
     * @return Number of items matching both criteria
     */
    public int findPriceRange(int n, int low, int high) {
        TreeSet<Item> items = descriptionMap.get(n);

        if (items == null) {
            return 0;
        }

        int count = 0;
        for (Item item : items) {
            if (item.price >= low && item.price <= high) {
                count++;
            }
        }

        return count;
    }

    /**
     * Removes specified descriptors from an item's description.
     * @param id The unique identifier of the item
     * @param list List of descriptors to remove
     * @return Sum of the actually removed descriptors, or 0 if item not found
     */
    public int removeNames(int id, List<Integer> list) {
        Item item = itemMap.get(id);
        if (item == null || list == null || list.isEmpty()) {
            return 0;
        }

        int sum = 0;
        List<Integer> descriptionsToRemove = new ArrayList<>();

        for (int description : list) {
            if (item.description.contains(description)) {
                descriptionsToRemove.add(description);

                TreeSet<Item> items = descriptionMap.get(description);
                if (items != null) {
                    sum += description;
                    items.remove(item);
                    if (items.isEmpty()) {
                        descriptionMap.remove(description);
                    }
                }
            }
        }

        item.description.removeAll(descriptionsToRemove);
        return sum;
    }
}
