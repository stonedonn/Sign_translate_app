from django.db import models

# Create your models here.
from django.db import models



# Create your models here.
class Myvideo(models.Model):
    model_pic = models.FileField(upload_to='video/')
