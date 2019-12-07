(ns baflo.tic-tac-toe.main 
  (:require ["readline" :as readline]))

(.emitKeypressEvents readline js/process.stdin)
(.setRawMode js/process.stdin true)


(def field (atom (vec (repeat 9 :empty))))
(def user (atom :circle))

(defn next-user []
  (swap! user {:circle :cross :cross :circle}))

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

(defn play [field user idx]
  (swap! field #(assoc %1 idx user)))

(defn empty-field [n] ["     "
                       (str "  " n "  ")
                       "     "])
(defn cross-field [n] [" \\ / "
                       "  X  "
                       " / \\ "])
(defn circle-field [n] [" /~\\ "
                        "(   )"
                        " \\~/ "])

(defn print-field [field]
  (println (clojure.string/join "\n" (apply interleave (partition 3 (map #(apply str %) (partition 3 (apply interleave
                                                                                                            (map-indexed #(({:empty empty-field :cross cross-field :circle circle-field} %2) (inc %1)) field)))))))))

(defn reload! []
  (println "App reloaded!"))

(defn main! []
  (println "Tic Tac Toe")
  (print-field @field)
  (.on js/process.stdin "keypress" #(when ((set (range 1 10)) (js/parseInt %1))
                                      (play field (next-user) (dec (js/parseInt %1)))
                                      (println "--------------")
                                      (print-field @field)
                                      (when (won? @field @user)
                                        (js/process.exit)))))
