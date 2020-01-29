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
    public class GoodsController : ControllerBase
    {
        private readonly GoodsContext _context;

        public GoodsController(GoodsContext context)
        {
            _context = context;
        }

        // GET: api/Goods
        [HttpGet]
        public IEnumerable<Goods> Getgoods()
        {
            return _context.goods;
        }

        // GET: api/Goods/5
        [HttpGet("{id}")]
        public async Task<IActionResult> GetGoods([FromRoute] int id)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            //var goods = await _context.goods.FindAsync(id);
            //var goods = await _context.goods.FirstOrDefaultAsync(i => i.id == id);
            var goods = await _context.goods.Include(g => g.images).FirstOrDefaultAsync(i => i.id == id);
            //var stream = new MemoryStream(goods.images);
            //IFormFile img = new FormFile(stream, 0, goods.images.Length, "name", "filename");\



            if (goods == null)
            {
                return NotFound();
            }

            return Ok(goods);
        }

        //// PUT: api/Goods/5
        //[HttpPut("{id}")]
        //public async Task<IActionResult> PutGoods([FromRoute] int id, [FromBody] Goods goods)
        //{
        //    if (!ModelState.IsValid)
        //    {
        //        return BadRequest(ModelState);
        //    }

        //    if (id != goods.id)
        //    {
        //        return BadRequest();
        //    }

        //    _context.Entry(goods).State = EntityState.Modified;

        //    try
        //    {
        //        await _context.SaveChangesAsync();
        //    }
        //    catch (DbUpdateConcurrencyException)
        //    {
        //        if (!GoodsExists(id))
        //        {
        //            return NotFound();
        //        }
        //        else
        //        {
        //            throw;
        //        }
        //    }

        //    return NoContent();
        //}

        //might need attention later
        // PUT: api/Goods/5
        [HttpPut("{id}")]
        public async Task<IActionResult> PutGoods([FromRoute] int id, [ModelBinder(BinderType = typeof(JsonModelBinder))] Goods goods, List<IFormFile> images)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            if (id != goods.id)
            {
                return BadRequest();
            }

            _context.Entry(goods).State = EntityState.Modified;

            try
            {
                await _context.SaveChangesAsync();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!GoodsExists(id))
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

        // POST: api/Goods
        [HttpPost]
        public async Task<IActionResult> PostGoods([ModelBinder(BinderType = typeof(JsonModelBinder))] Goods goods, List<IFormFile> images)//List<IFormFile> images
        {
            //-------------------------------------------------------------------------------
            try
            {
                if (!ModelState.IsValid)
                {
                    return BadRequest(ModelState);
                }
                //if (_context.Image.)
                //    _context.Entry().State = EntityState.Modified;

                _context.goods.Add(goods);

                if (images != null)
                {
                    foreach (var img in images)
                    {
                        Image item = new Image();
                        //item.goodsId = 1;
                        if (img.Length > 0)
                        {
                            using (var stream = new MemoryStream())
                            {
                                await img.CopyToAsync(stream);
                                item.image = stream.ToArray();
                                //item.id = 1;
                                item.goodsId = goods.id;
                                //item.goods = goods;
                                _context.Image.Add(item);
                            }
                            //goods.images.Add(item);
                        }
                    }
                }
                //-------------------------------------------------------------------
                //if (img != null)
                //{
                //    if (img.Length > 0)
                //    {
                //        using (var stream = new MemoryStream())
                //        {
                //            await img.CopyToAsync(stream);
                //            item.image = stream.ToArray();
                //        }
                //    }
                //}
                //}

                //_context.goods.Add(goods);
                await _context.SaveChangesAsync();

                return CreatedAtAction("GetGoods", new { id = goods.id }, goods);
            }
            catch (Exception)
            {
                return NoContent();

            }
        }

        // DELETE: api/Goods/5
        [HttpDelete("{id}")]
        public async Task<IActionResult> DeleteGoods([FromRoute] int id)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            //var goods = await _context.goods.FindAsync(id);
            var goods = await _context.goods.FirstOrDefaultAsync(i => i.id == id);
            List<Image> images = _context.Image.Where(i => i.goodsId == id).ToList();
            if (goods == null)
            {
                return NotFound();
            }

            _context.goods.Remove(goods);
            foreach (var img in images) {
                _context.Image.Remove(img);
            }
            await _context.SaveChangesAsync();

            return Ok(goods);
        }

        private bool GoodsExists(int id)
        {
            return _context.goods.Any(e => e.id == id);
        }
    }
}