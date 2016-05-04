import Image
import ImageDraw

boxh = (0,0,32,8)
boxg = (0,8,8,24)
boxd = (24,8,32,24)
boxb = (0,24,32,32)

for x in range(2,9):
  for y in range(1,x+1):
    name = "a"+str(x)+'_'+str(y)
    image = Image.open(name + '.png')
    image.load()
    image2 = image.crop(boxh)
    image2.save(name + '_h.png')
    image2 = image.crop(boxg)
    image2.save(name + '_g.png')
    image2 = image.crop(boxd)
    image2.save(name + '_d.png')
    image2 = image.crop(boxb)
    image2.save(name + '_b.png')
