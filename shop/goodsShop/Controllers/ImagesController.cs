using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using goodsShop.Models;
using System.IO;
using Microsoft.AspNetCore.Http.Internal;
using BrunoZell.ModelBinding;

namespace goodsShop.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class ImagesController : ControllerBase
    {
        private readonly GoodsContext _context;

        public ImagesController(GoodsContext context)
        {
            _context = context;
        }

        // GET: api/Images
        [HttpGet]
        public IEnumerable<Image> GetImage()
        {
            return _context.Image;
        }

        // GET: api/Images/5
        [HttpGet("{id}")]
        public async Task<IActionResult> GetImage(long id)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            var image = await _context.Image.FindAsync(id);
            //var image = await _context.Image.Include(i => i.goods).FirstOrDefaultAsync(i => i.id == id);

            //var stream = new MemoryStream(image.image);
            //IFormFile img = new FormFile(stream, 0, image.image.Length, "name", "filename");

            if (image == null)
            {
                return NotFound();
            }

            return Ok(image);
        }

        // PUT: api/Images/5
        [HttpPut("{id}")]
        public async Task<IActionResult> PutImage([FromRoute] long id, [FromBody] Image image)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            if (id != image.id)
            {
                return BadRequest();
            }

            _context.Entry(image).State = EntityState.Modified;

            try
            {
                await _context.SaveChangesAsync();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!ImageExists(id))
                {
                    return NotFound();
                }
                else
                {
                    throw;
                }
            }

            return NoContent();
        }

        // POST: api/Images
        [HttpPost]
        public async Task<IActionResult> PostImage([ModelBinder(BinderType = typeof(JsonModelBinder))] Image item, IFormFile img)
        {
            try
            {
                //Image item = new Image();
                //foreach (var img in images)
                //{
                if (img != null)
                {
                    if (img.Length > 0)
                    {
                        using (var stream = new MemoryStream())
                        {
                            await img.CopyToAsync(stream);
                            item.image = stream.ToArray();
                        }
                    }
                }
                //}

                _context.Image.Add(item);
                await _context.SaveChangesAsync();

                return CreatedAtAction("GetImage", new { id = item.id }, item);
            }
            catch (Exception)
            {
                return NoContent();
                
            }
        }

        // DELETE: api/Images/5
        [HttpDelete("{id}")]
        public async Task<IActionResult> DeleteImage([FromRoute] long id)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            var image = await _context.Image.FindAsync(id);
            if (image == null)
            {
                return NotFound();
            }

            _context.Image.Remove(image);
            await _context.SaveChangesAsync();

            return Ok(image);
        }

        private bool ImageExists(long id)
        {
            return _context.Image.Any(e => e.id == id);
        }
    }
}