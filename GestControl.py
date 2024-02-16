import cv2
import mediapipe as m
import pyautogui
m_draw = m.solutions.drawing_utils
m_hands = m.solutions.hands

tips = [4, 8, 12, 16, 20]
state = None
width_cam, height_cam = 854, 480
cap = cv2.VideoCapture(0)
cap.set(3, width_cam)
cap.set(4, height_cam)
  
def fingerPosition(image, handNo=0):
    list = []
    if results.multi_hand_landmarks:
        myHand = results.multi_hand_landmarks[handNo]
        for id, l in enumerate(myHand.landmark):
            h, w = image.shape
            cx, cy = int(l.x * w), int(l.y * h)
            list.append([id, cx, cy])
    return list

with m_hands.Hands(
    min_detection_confidence=0.8,
     min_tracking_confidence=0.5) as hand:
    while cap.isOpened():
        success, image = cap.read()
        if not success:
            print("Ignoring empty camera frame.")
            continue
    
        image = cv2.cvtColor(cv2.flip(image, 1), cv2.COLOR_BGR2RGB)
        image.flags.writeable = False
        results = hand.process(image)
    
        image.flags.writeable = True
        image = cv2.cvtColor(image, cv2.COLOR_RGB2BGR)
        if results.multi_hand_landmarks:
            for hand_landmarks in results.multi_hand_landmarks:
                m_draw.draw_landmarks(
                    image, hand_landmarks, m_hands.HAND_CONNECTIONS)
        list = fingerPosition(image)
    
        if len(list) != 0:
            fingers = []
            for id in range(1, 5):
                if list[tips[id]][2] < list[tips[id] - 2][2]:
                    fingers.append(1)
                if (list[tips[id]][2] > list[tips[id] - 2][2] ): 
                    fingers.append(0)

            totalFingers = fingers.count(1)
            print(totalFingers)
     	
            if totalFingers == 4:
                state = "Play"

            if totalFingers == 0 and state == "Play":
                state = "Pause"
                pyautogui.press('space')
                print("Space")
            if totalFingers == 1:
                if list[8][1]<300:
                    pyautogui.press('left')
                if list[8][1]>400:
                    pyautogui.press('Right')
            if totalFingers == 2:
                if list[9][2] < 210:
                    print("Up")
                    pyautogui.press('Up')
                if list[9][2] > 230:
                    print("Down")
                    pyautogui.press('Down')
        cv2.imshow("Media Controller", image)

        key = cv2.waitKey(1) & 0xFF
        if key == ord("q"):
            break
    cv2.destroyAllWindows()