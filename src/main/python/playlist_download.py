__author__ = 'lawrence'

from pytube import YouTube
from pytube import Playlist

#YouTube('https://youtu.be/9bZkp7q19f0').streams.first().download("C:\Users\lawrence\uk_hill\music")

pl = Playlist("https://www.youtube.com/playlist?list=PLL7SmUjap7Vh3uf4zmElfSxLqIdgPiV_z")
#links = pl.parse_links()
pl.populate_video_urls()
urls = pl.video_urls

for url in urls:
    yt = YouTube(url)
    print yt.title
    stream = yt.streams.filter(only_audio=True).order_by('resolution').all()
    # stream = yt.streams.all()
    print stream
    i = 0