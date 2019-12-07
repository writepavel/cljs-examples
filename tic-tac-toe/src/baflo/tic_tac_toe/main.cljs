(ns baflo.tic-tac-toe.main)

(def field (atom (repeat 9 :empty)))

(defn get-fields [field indices]
  (map field indices))
(defn anti-diagonal [field]
  (map #(nth field %) (range 0 9 4)))
(defn diagonal [field]
  (map #(nth field %) (range 2 7 2)))
(defn rows [field]
  (partition 3 3 field))
(defn cols [field]
  (for [colIndex (range 3)] (flatten (partition 1 3 (drop colIndex field)))))

(def diagonals (juxt diagonal anti-diagonal))
(defn all-strikes [field]
  (reduce concat ((juxt diagonals rows cols) field)))

(defn won? [field user]
  (true? (some #(= (repeat 3 user) %) (all-strikes field))))

(defn reload! []
  (println "App reloaded!"))

(defn main! []
  (println "Tic Tac Toe!"))
