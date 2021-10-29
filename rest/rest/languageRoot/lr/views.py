from django.shortcuts import render

# Create your views here.
from django.http import HttpResponse
from django.views.decorators.csrf import csrf_exempt
from rest_framework.authentication import SessionAuthentication
from rest_framework.decorators import api_view
from rest_framework.generics import CreateAPIView
from . import models
from . import serializers
from rest_framework.permissions import IsAuthenticated, AllowAny, IsAdminUser
import os

from rest_framework.response import Response
# Create your views here.
import cv2
import mediapipe as mp
import numpy as np
import natsort
from glob import glob
import simplejson
from tensorflow.keras.models import load_model

new_model = load_model("model/testmodel.h5")
pre_ans_str = ''


class ImageCreateAPIView(CreateAPIView):
    serializer_class = serializers.ImageSerializer
    queryset = models.Myvideo.objects.all()
    authentication_class = (SessionAuthentication)
    permission_class = (AllowAny)



    
# @csrf_exempt
# @api_view(['GET', 'POST'])

def video_upload_view(request):
    # if request.method == 'POST':
    #     serializer = serializers.ImageSerializer(data=request.data, many=True)
    # if (serializer.is_valid()): serializer.save()
    # return Response(serializer.data, status=200)
    # return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

    # ans = post_api()
    path = r'C:\list_in\rest\languageRoot\lr\video\video\test.avi'
    # os.path.isfile(filePath)  # 해당 파일이 있는지 확인
    # #  영상 객체 가져오기
    mp_holistic = mp.solutions.holistic
    mp_drawing = mp.solutions.drawing_utils

    def mediapipe_detection(image, model):
        results = model.process(image)
        image.flags.writeable = True
        image = cv2.cvtColor(image, cv2.COLOR_RGB2BGR)
        return image, results

    def draw_landmarks(image, results):
        mp_holistic = mp.solutions.holistic
        mp_drawing = mp.solutions.drawing_utils
        mp_drawing.draw_landmarks(image, results.face_landmarks, mp_holistic.FACEMESH_CONTOURS)
        mp_drawing.draw_landmarks(image, results.pose_landmarks, mp_holistic.POSE_CONNECTIONS)
        mp_drawing.draw_landmarks(image, results.left_hand_landmarks, mp_holistic.HAND_CONNECTIONS)
        mp_drawing.draw_landmarks(image, results.right_hand_landmarks, mp_holistic.HAND_CONNECTIONS)

    def extract_keypoints(results):
        pose = np.array([[res.x, res.y, res.z, res.visibility] for res in
                         results.pose_landmarks.landmark]).flatten() if results.pose_landmarks else np.zeros(33 * 4)
        face = np.array([[res.x, res.y, res.z] for res in
                         results.face_landmarks.landmark]).flatten() if results.face_landmarks else np.zeros(468 * 3)
        lhand = np.array([[res.x, res.y, res.z] for res in
                          results.left_hand_landmarks.landmark]).flatten() if results.left_hand_landmarks else np.zeros(
            21 * 3)
        rhand = np.array([[res.x, res.y, res.z] for res in
                          results.right_hand_landmarks.landmark]).flatten() if results.right_hand_landmarks else np.zeros(
            21 * 3)
        return np.concatenate([pose, face, lhand, rhand])


    cap = cv2.VideoCapture(path,cv2.CAP_FFMPEG)
    success, image = cap.read()
    count = 0
    while success:
        cv2.imwrite(r'C:\list_in\rest\languageRoot\lr\video\frame%d.jpg' % count, image)
        success, image = cap.read()
        print(success)
        print('read a new frame:', success)
        count = count + 1
    else:
        print("완료!!!")

    print('완료!!!1')

    data = glob(r'C:\list_in\rest\languageRoot\lr\video\*.jpg')
    data = natsort.natsorted(data)

    list = []
    for i in data:
        list.append(i)

    action = np.array(
        ['hello', 'I', 'You', 'We', 'money', 'Sorry', 'welldone', 'see', 'no', 'sports', 'get', 'appear', 'go', 'come',
         'lost', 'time', 'win', 'election', 'meet', 'school', 'drink', 'together', 'promise', 'late', 'think',
         'beautiful', 'standby'])

    threshold = 0.3

    print('완료!!!2')

    sequence_length = len(list)
    sequence = []
    predictions = []
    sentence = []
    with mp_holistic.Holistic(min_detection_confidence=0.5, min_tracking_confidence=0.5) as holistic:
        for i in list:
            # for frame_num in range(sequence_length):
                img = cv2.imread(i)
                image, results = mediapipe_detection(img, holistic)
                draw_landmarks(image, results)
                keypoints = extract_keypoints(results)
                sequence.insert(0, keypoints)
                sequence = sequence[:30]
                if len(sequence) == 30:
                    res = new_model.predict(np.expand_dims(sequence, axis=0))[0]
                    predictions.append(np.argmax(res))

        cap.release()
        cv2.destroyAllWindows()
    
    count_list = []
    for x in predictions:
        count_list.append(predictions.count(x))

    pre_ans = predictions[count_list.index(max(count_list))]
    global pre_ans_str
    if pre_ans == 0:
        pre_ans_str = "안녕하세요"
    elif pre_ans == 1:
        pre_ans_str = "나"
    elif pre_ans == 2:
        pre_ans_str = "너"
    elif pre_ans == 3:
        pre_ans_str = "우리"
    elif pre_ans == 4:
        pre_ans_str = "돈"
    elif pre_ans == 5:
        pre_ans_str = "미안합니다"
    elif pre_ans == 6:
        pre_ans_str = "잘하다"
    elif pre_ans == 7:
        pre_ans_str = "보다"
    elif pre_ans == 8:
        pre_ans_str = "아니다"
    elif pre_ans == 9:
        pre_ans_str = "운동"
    elif pre_ans == 10:
        pre_ans_str = "가지다"
    elif pre_ans == 11:
        pre_ans_str = "나타나다"
    elif pre_ans == 12:
        pre_ans_str = "가다"
    elif pre_ans == 13:
        pre_ans_str = "오다"
    elif pre_ans == 14:
        pre_ans_str = "잃어버리다,분실하다"
    elif pre_ans == 15:
        pre_ans_str = "시간"
    elif pre_ans == 16:
        pre_ans_str = "이기다"
    elif pre_ans == 17:
        pre_ans_str = "선거"
    elif pre_ans == 18:
        pre_ans_str = "만나다"
    elif pre_ans == 19:
        pre_ans_str = "학교"
    elif pre_ans == 20:
        pre_ans_str = "마시다"
    elif pre_ans == 21:
        pre_ans_str = "함께"
    elif pre_ans == 22:
        pre_ans_str = "약속"
    elif pre_ans == 23:
        pre_ans_str = "늦다"
    elif pre_ans == 24:
        pre_ans_str = "생각하다"
    elif pre_ans == 25:
        pre_ans_str = "아름답다"
    elif pre_ans == 26:
        pre_ans_str = "어떠한 동작도 인식되지 않았습니다."
    else:
        pre_ans_str = "다시 시도해 주세요!"

    print(pre_ans_str)
    print(predictions)
    awb_dict = {'pre_ans_str': pre_ans_str}
    file_list = os.listdir("C:/list_in/rest/languageRoot/lr/video")

    for file in file_list:
        filename, file_extension = os.path.splitext(file)
        if file_extension == ".jpg":
            file_path = os.path.join("C:/list_in/rest/languageRoot/lr/video", file)
            os.remove(file_path)

    os.remove("C:/list_in/rest/languageRoot/lr/video/video/test.avi")
    return HttpResponse(simplejson.dumps(awb_dict))


   