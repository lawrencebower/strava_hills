__author__ = 'lawrence'

from pytube import YouTube

# yt = YouTube('https://youtu.be/-p27dCVk-w0')
# yt = YouTube('https://youtu.be/4ocBfChkQgE')#whiteleaf
yt = YouTube('https://youtu.be/fxTtASt3oeM')

print yt.title
unfiltered = yt.streams
streams = unfiltered.filter().order_by('resolution').desc().all()
# yt.streams.filter(progressive=True, file_extension='webm').order_by('resolution').desc().first().download("C:\Users\lawrence\uk_hill\music")
for stream in streams:
    if(stream.includes_audio_track & stream.includes_video_track):
        print "downloading "
        stream.download("C:\Users\lawrence\uk_hill\music", yt.title + "_video_" + stream.resolution)
        break

print "Finished"
