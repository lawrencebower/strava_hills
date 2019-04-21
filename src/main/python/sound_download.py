import os
import re
import sound_download

__author__ = 'lawrence'

from pytube import YouTube

def download(title, stream):

    filename = title + "_music_" + stream.abr
    #stream.default
    root = "C:\Users\lawrence\uk_hill\music\\"
    # root = "F:\music\\"
    filePath = root + filename + "." + stream.subtype
    if(not os.path.exists(filePath)):
        print "downloading " + filePath
        stream.download(root, filename)
    else:
        print "already exists " + filePath

def getBestStream (title, streams):

    bestStream = None
    bestRate = 0

    for stream in streams:
        bitString = stream.abr
        # print bitString

        if (bitString is not None):
            rate = int(bitString.split("kbps")[0])
            # print rate
            if rate > bestRate:
                # print("Assigning new best rate :{:d}".format(rate))
                bestStream = stream
                bestRate = rate

    download(title, bestStream)

def test():
    # yt = YouTube('https://youtu.be/mT7lQs7g5S8')
    # yt = YouTube('https://www.youtube.com/watch?v=0ztdqrMRiKc&t=24s')
    yt = YouTube("https://youtu.be/78BFFq_8XvM")

    # title = "hello"
    title = sound_download.fixString(yt.title)
    print title
    allStreams = yt.streams
    streams = getAudioStreams(allStreams)

    getBestStream(title, streams)
    print "Finished"


def getAudioStreams(allStreams):
    streams = allStreams.filter(only_audio=True).all()
    if (not streams):
        print "No audio streams"
        streams = allStreams.all()
    return streams


if __name__ == '__main__':
    test()


def fixString(string):
    fixed = re.sub('[^A-Za-z0-9\s]+', '', string)
    fixed = re.sub('\s', '_', fixed)
    return  fixed