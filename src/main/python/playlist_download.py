__author__ = 'lawrence'

from pytube import YouTube
from pytube import Playlist
import sound_download
import re

#YouTube('https://youtu.be/9bZkp7q19f0').streams.first().download("C:\Users\lawrence\uk_hill\music")

pl = Playlist("https://www.youtube.com/playlist?list=PLL7SmUjap7Vh3uf4zmElfSxLqIdgPiV_z")
#links = pl.parse_links()
pl.populate_video_urls()
urls = pl.video_urls

for url in urls:
    yt = YouTube(url)
    title =  sound_download.fixString(yt.title)
    print title
    streams = sound_download.getAudioStreams(yt.streams)
    sound_download.getBestStream(title, streams)
    # i = 0

