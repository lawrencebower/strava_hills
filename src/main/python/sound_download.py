__author__ = 'lawrence'

from pytube import YouTube

# yt = YouTube('https://youtu.be/mT7lQs7g5S8').streams.first().download("C:\Users\lawrence\uk_hill\music")
yt = YouTube('https://youtu.be/mT7lQs7g5S8')
# yt = YouTube('https://youtu.be/-p27dCVk-w0')

print yt.title
collection = yt.streams.filter(only_audio=True, file_extension="mp4").order_by('bitrate').desc()
# collection = yt.streams
streams = collection.all()
# stream.download("C:\Users\lawrence\uk_hill\music")

def download(stream):
    print "downloading"
    stream.download("C:\Users\lawrence\uk_hill\music", yt.title + "_music_" + stream.abr)

for stream in streams:
    print stream.abr
    if stream.abr == "160kbps":
        download(stream)
        break
    elif stream.abr == "128kbps":
        download(stream)
        break
    else:
        print stream.abr

print "Finished"

