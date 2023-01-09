(ns clojure-basics.agents)

;; agents
(defn agent-x
  []

  (let [tickets-sold (agent 0)]
    (send tickets-sold + 55)
    (println "Tickets count = " @tickets-sold)

    (send tickets-sold + 66)
    (await-for 100 tickets-sold)
    (println "Tickets count = " @tickets-sold)

    (shutdown-agents)))

(agent-x)
