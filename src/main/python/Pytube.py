__author__ = 'lawrence'

import pytube

from pytube import YouTube
from pytube import Playlist

#YouTube('https://youtu.be/9bZkp7q19f0').streams.first().download("C:\Users\lawrence\uk_hill\music")

pl = Playlist("https://www.youtube.com/playlist?list=PLL7SmUjap7Vh3uf4zmElfSxLqIdgPiV_z")
#links = pl.parse_links()
pl.populate_video_urls()
urls = pl.video_urls

print urls