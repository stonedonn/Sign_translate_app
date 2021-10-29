from rest_framework import serializers
from .models import Myvideo


class ImageSerializer(serializers.ModelSerializer):
    class Meta:
        model = Myvideo
        fields = ['model_pic']