(ns highway-to-hell.song
  (:require [overtone.live :refer :all]
            [overtone.inst.drum :refer :all]
            [overtone.synth.stringed :refer :all]
            [overtone.music.pitch]
            [overtone.inst.synth]
            [overtone.inst.io]
            [shadertone.tone :as t]
            [leipzig.melody :refer :all]
            [leipzig.scale :as scale]
            [leipzig.live :as live]
            [leipzig.chord :as chord]
            [leipzig.temperament :as temperament]))


;; Instruments
(def g (guitar))
(def electric-g (guitar))

(comment
  (guitar-pick g 3 1)
  (guitar-pick electric-g 3 1)
  (guitar-strum g :G :down)
  (live/play-note {:part :electric-rhythm-guitar
                   :chord :G
                   :chord-direction :down})

  (live/play-note {:part :beat
                   :drum :kick}))

(ctl electric-g :pre-amp 5.0 :distort 0.96
     :lp-freq 5000 :lp-rq 0.25
     :rvb-mix 0.5 :rvb-room 0.7 :rvb-damp 0.4)

(def drum-kit
  {:kick #(do
            (kick)
            (kick3)
            (kick2 :amp 2))
   :snare #(do
             (snare :amp 2)
             (snare2)
             (tone-snare))
   :ch #(do
          (closed-hat 0.8)
          (closed-hat2 0.6)
          (hat3 0.4 0.3))})

; Arrangement
(defmethod live/play-note :beat [{drum-name :drum}]
  ((drum-name drum-kit)))

(defmethod live/play-note :rhythm-guitar
  [{chord :chord direction :chord-direction speed :strum-speed
    :or {direction :down speed 0.07}}]
  (when-not (nil? chord)
    (guitar-strum g chord direction speed)))

(defmethod live/play-note :electric-rhythm-guitar
  [{chord :chord direction :chord-direction speed :strum-speed
    :or {direction :down speed 0.01}}]
  (guitar-strum electric-g chord direction speed))

;Composition


(def highway
  (->> (rhythm [1 1 1/2 3/2
                4
                1 1 1/2 3/2
                4
                1 1 1/2 3/2
                1 1 1/2 3/2
                3/2 1/2 1 3/2 1/2
                3])
       (having :chord [[-1 0 2 2 2 -1]
                       [-1 0 2 2 2 -1]
                       [-1 0 2 2 2 -1]
                       [-1 -1 -1 -1 -1 -1]
                       [-1 -1 -1 -1 -1 -1]
                       [2 -1 0 2 3 -1]
                       [2 -1 0 2 3 -1]
                       [3 -1 0 0 3 -1]
                       [-1 -1 -1 -1 -1 -1]
                       [-1 -1 -1 -1 -1 -1]
                       [2 -1 0 2 3 -1]
                       [2 -1 0 2 3 -1]
                       [3 -1 0 0 3 -1]
                       [-1 -1 -1 -1 -1 -1]
                       [2 -1 0 2 3 -1]
                       [2 -1 0 2 3 -1]
                       [3 -1 0 0 3 -1]
                       [-1 -1 -1 -1 -1 -1]
                       [2 -1 0 2 3 -1]
                       [-1 -1 -1 -1 -1 -1]
                       [-1 0 2 2 2 -1]
                       [-1 0 2 2 2 -1]
                       [-1 -1 -1 -1 -1 -1]
                       [-1 -1 -1 -1 -1 -1]])
       (having :chord-direction (concat (take 10 (cycle [:down :up :down :down :down]))
                                        (take 8 (cycle [:down :up :down :down]))
                                        [:down :down :down :up :down :down]))
       (all :part :electric-rhythm-guitar)
       (with (->> (rhythm (repeat 16 2))
                  (all :part :beat)
                  (having :drum (cycle [:kick :snare]))
                  (with (->> (rhythm (repeat 16 2))
                             (all :part :beat)
                             (having :drum (repeat :ch))))
                  (where :time #(- % 1))))
       (tempo (bpm 200))))

; Track
(def track
  highway)

(defn -main []
  (live/play track))

(comment
  ; Loop the track, allowing live editing.
  (live/jam (var highway))
  (live/stop)
  (live/play highway)
  )
