package com.zpj.common;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigInteger;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Id;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.jdbc.Work;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Administrator
 * 
 */

@Repository
@SuppressWarnings("unchecked")
@Transactional
public class BaseDao<T extends Serializable> {
	@Autowired
	private SessionFactory sessionFactory;
	/**
	 * 创建一个Class的对象来获取泛型的class
	 */
	private Class<?> clz;
	
	
//	public static Class getSuperClassGenricType(Class clazz) {  
//        return getSuperClassGenricType(clazz, 0);  
//    }  
//	public static Class getSuperClassGenricType(Class clazz, int index) throws IndexOutOfBoundsException {  
//		  
//        Type genType = clazz.getGenericSuperclass();  
//  
//        if (!(genType instanceof ParameterizedType)) {  
//            return Object.class;  
//        }  
//  
//        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();  
//  
//        if (index >= params.length || index < 0) {  
//            return Object.class;  
//        }  
//        if (!(params[index] instanceof Class)) {  
//            return Object.class;  
//        }  
//        return (Class) params[index];  
//    }  
	public Class<?> getClz() {
		if (clz == null) {
			// 获取泛型的Class对象
			clz = ((Class<?>) (((ParameterizedType) (this.getClass().getGenericSuperclass()))
					.getActualTypeArguments()[0]));
		}
		return clz;
	}

	protected Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	public T add(T t) {
		getSession().save(t);
		return t;
	}

	public void update(T t) {
		getSession().update(t);
	}

	public void delete(int id) {
		getSession().delete(this.load(id));

	}

	public void delete(T t) {
		if(t!=null){
		getSession().delete(t);
		}
	}

	public T load(int id) {
		return (T) getSession().get(getClz(), id);
	}

	public T getByHQL(String hql) {
		return (this.list(hql).size()==0)?null:this.list(hql).get(0);
	}

	public void merge(T t, String id) {
		getSession().merge(id, t);
	}

	public T get(Serializable id,Class<T> entityClass) {
		return (T) getSession().get(entityClass, id);
	}
	
	public T get(Serializable id) {
		return (T) getSession().get(getClz(), id);
	}

	public List<T> list(String hql, Object[] args) {
		return this.list(hql, args, null);
	}

	public List<T> list(String hql, Object arg) {
		return this.list(hql, new Object[] { arg });
	}

	public List<T> list(String hql) {
		return this.list(hql, null);
	}

	private String initSort(String hql) {
		String order = SystemContext.getOrder();
		String sort = SystemContext.getSort();
		if (sort != null && !"".equals(sort.trim())) {
			hql += " order by " + sort;
			if (!"desc".equals(order))
				hql += " asc";
			else
				hql += " desc";
		}
		return hql;
	}

	@SuppressWarnings("rawtypes")
	private void setAliasParameter(Query query, Map<String, Object> alias) {
		if (alias != null) {
			Set<String> keys = alias.keySet();
			for (String key : keys) {
				Object val = alias.get(key);
				if (val instanceof Collection) {
					// 查询条件是列表
					query.setParameterList(key, (Collection) val);
				} else {
					query.setParameter(key, val);
				}
			}
		}
	}

	private void setParameter(Query query, Object[] args) {
		if (args != null && args.length > 0) {
			int index = 0;
			for (Object arg : args) {
				query.setParameter(index++, arg);
			}
		}
	}

	public List<T> list(String hql, Object[] args, Map<String, Object> alias) {
		hql = initSort(hql);
		Query query = getSession().createQuery(hql);
		setAliasParameter(query, alias);
		setParameter(query, args);
		return query.list();
	}

	public List<T> listByAlias(String hql, Map<String, Object> alias) {
		return this.list(hql, null, alias);
	}

	public Pager<T> find(String hql, Object[] args, int start, int limit) {
		return this.find(hql, args, null, start, limit);
	}

	public Pager<T> find(String hql, Object arg, int start, int limit) {
		return this.find(hql, new Object[] { arg }, start, limit);
	}

	public Pager<T> find(String hql, int start, int limit) {
		return this.find(hql, null, start, limit);
	}

	@SuppressWarnings("rawtypes")
	private void setPagers(Query query, Pager page) {
		query.setFirstResult(page.getStart()).setMaxResults(page.getLimit());
	}

	private String getCountHql(String hql, boolean isHql) {
		
		String e = hql.substring(hql.indexOf("from"));
		int orderby=e.indexOf("order");
		if(orderby!=-1){
			e=e.substring(0,orderby);
		}
		String c = "select count(*) " + e;
		if (isHql)
			c.replaceAll("fetch", "");
		return c;
	}

	public Pager<T> find(String hql, Object[] args, Map<String, Object> alias, int start, int limit) {
		hql = initSort(hql);
		String cq = getCountHql(hql, true);
		Query cquery = getSession().createQuery(cq);
		Query query = getSession().createQuery(hql);
		// 设置别名参数
		setAliasParameter(query, alias);
		setAliasParameter(cquery, alias);
		// 设置参数
		setParameter(query, args);
		setParameter(cquery, args);
		Pager pages = new Pager();
		pages.setStart(start);
		pages.setLimit(limit);
		setPagers(query, pages);
		List<T> datas = query.list();
		pages.setDatas(datas);
		long total = (Long) cquery.uniqueResult();
		pages.setTotal(total);
		return pages;
	}

	public Pager<T> findByAlias(String hql, Map<String, Object> alias, int start, int limit) {
		return this.find(hql, null, alias, start, limit);
	}

	public Object queryObject(String hql, Object[] args) {
		return this.queryObject(hql, args, null);
	}

	public Object queryObject(String hql, Object arg) {
		return this.queryObject(hql, new Object[] { arg });
	}

	public Object queryObject(String hql) {
		return this.queryObject(hql, null);
	}

	public void updateByHql(String hql, Object[] args) {
		Query query = getSession().createQuery(hql);
		setParameter(query, args);
		query.executeUpdate();
	}

	public void updateByHql(String hql, Object arg) {
		this.updateByHql(hql, new Object[] { arg });
	}

	public void updateByHql(String hql) {
		this.updateByHql(hql, null);
	}

	public <N extends Object> List<N> listBySql(String sql, Object[] args, Class<?> clz, boolean hasEntity) {
		return this.listBySql(sql, args, null, clz, hasEntity);
	}

	public <N extends Object> List<N> listBySql(String sql, Object arg, Class<?> clz, boolean hasEntity) {
		return this.listBySql(sql, new Object[] { arg }, clz, hasEntity);
	}

	public <N extends Object> List<N> listBySql(String sql, Class<?> clz, boolean hasEntity) {
		return this.listBySql(sql, null, clz, hasEntity);
	}

	public <N extends Object> List<N> listBySql(String sql, Object[] args, Map<String, Object> alias, Class<?> clz,
			boolean hasEntity) {
		sql = initSort(sql);
		SQLQuery sq = getSession().createSQLQuery(sql);
		setAliasParameter(sq, alias);
		setParameter(sq, args);
		if (hasEntity) {
			sq.addEntity(clz);
		} else
			sq.setResultTransformer(Transformers.aliasToBean(clz));
		return sq.list();
	}

	public <N extends Object> List<N> listByAliasSql(String sql, Map<String, Object> alias, Class<?> clz,
			boolean hasEntity) {
		return this.listBySql(sql, null, alias, clz, hasEntity);
	}

	public <N extends Object> Pager<N> findBySql(String sql, Object[] args, Class<?> clz, boolean hasEntity) {
		return this.findBySql(sql, args, null, clz, hasEntity);
	}

	public <N extends Object> Pager<N> findBySql(String sql, Object arg, Class<?> clz, boolean hasEntity) {
		return this.findBySql(sql, new Object[] { arg }, clz, hasEntity);
	}

	public <N extends Object> Pager<N> findBySql(String sql, Class<?> clz, boolean hasEntity) {
		return this.findBySql(sql, null, clz, hasEntity);
	}

	public <N extends Object> Pager<N> findBySql(String sql, Object[] args, Map<String, Object> alias, Class<?> clz,
			boolean hasEntity) {
		sql = initSort(sql);
		String cq = getCountHql(sql, false);
		SQLQuery sq = getSession().createSQLQuery(sql);
		SQLQuery cquery = getSession().createSQLQuery(cq);
		setAliasParameter(sq, alias);
		setAliasParameter(cquery, alias);
		setParameter(sq, args);
		setParameter(cquery, args);
		Pager<N> pages = new Pager<N>();
		setPagers(sq, pages);
		if (hasEntity) {
			sq.addEntity(clz);
		} else {
			sq.setResultTransformer(Transformers.aliasToBean(clz));
		}
		List<N> datas = sq.list();
		pages.setDatas(datas);
		long total = ((BigInteger) cquery.uniqueResult()).longValue();
		pages.setTotal(total);
		return pages;
	}

	public <N extends Object> Pager<N> findByAliasSql(String sql, Map<String, Object> alias, Class<?> clz,
			boolean hasEntity) {
		return this.findBySql(sql, null, alias, clz, hasEntity);
	}

	public Object queryObject(String hql, Object[] args, Map<String, Object> alias) {
		Query query = getSession().createQuery(hql);
		setAliasParameter(query, alias);
		setParameter(query, args);
		return query.uniqueResult();
	}

	public Object queryObjectByAlias(String hql, Map<String, Object> alias) {
		return this.queryObject(hql, null, alias);
	}

	public List<Map<String, Object>> findByPageMap(final String hql, final Object[] params, final int pageNo,
			final int maxResults) throws Exception {
		Query queryObject = getSession().createQuery(hql);
		if (pageNo < 1)
			throw new Exception("pageNo 必须大于 0");
		int firstResult = (pageNo - 1) * maxResults;
		if (params != null) {
			for (int i = 0; i < params.length; i++) {
				queryObject.setParameter(i, params[i]);
			}
		}
		queryObject.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		queryObject.setFirstResult(firstResult);
		queryObject.setMaxResults(maxResults);
		return (List<Map<String, Object>>) queryObject.list();
	}

	/**
	 * 返回对象
	 * 
	 * @Title findMapObjBySql
	 * @param sql
	 * @param conditions
	 * @param page
	 * @param pageSize
	 * @return
	 * @author zpj
	 * @time 2016-1-22 下午3:34:45
	 */
	public List findMapObjBySql(final String sql, final List<String> conditions, final int page, final int pageSize) {

		List list = null;
		Session session = null;
		try {
			session = sessionFactory.getCurrentSession();
			Query query = session.createSQLQuery(sql);
			if (conditions != null && conditions.size() > 0) {
				for (int i = 0; i < conditions.size(); i++) {
					if (conditions.get(i) != null && !conditions.get(i).equals("")) {
						query.setString(i, conditions.get(i));
					}
				}
			}
			query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			query.setFirstResult((page - 1) * pageSize);
			query.setMaxResults(pageSize);
			list = query.list();
			// session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// session.close();
		}
		return list;
	}

	public int findListCountBySql(final String sql, final List<String> conditions) {
		Session session = null;
		session = sessionFactory.getCurrentSession();
		Query query = session.createSQLQuery(sql);
		if (conditions != null && conditions.size() > 0) {
			for (int i = 0; i < conditions.size(); i++) {
				if (conditions.get(i) != null && !conditions.get(i).equals("")) {
					query.setString(i, conditions.get(i));
				}
			}
		}
		List list = query.list();
		if (list != null && list.size() > 0) {
			return Integer.parseInt(list.get(0).toString());
		} else {
			return 0;
		}
	}

	public List<T> findBySqlT(String sql, Class<T> t) {
		List<T> list = null;
		Session session = null;
		try {
			session = sessionFactory.getCurrentSession();
			Query query = sessionFactory.getCurrentSession().createSQLQuery(sql).addEntity(t);
			list = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
		if (list != null && list.size() > 0) {
			return list;
		} else {
			return null;
		}
	}	
	
	
	/**
	 * 单表查询分页
	 * @Title findPageDateSqlT
	 * @param tablename
	 * @param column
	 * @param conditions
	 * @param px
	 * @param page
	 * @param pagesize
	 * @param t
	 * @return
	 * @author zpj
	 * @time 2018年3月7日 下午1:09:43
	 */

	public MyPage findPageDateSqlT(String tablename, String columns,Map<String, Object> conditions, Map<String, Object> px,
			Integer page, Integer pagesize, Class<T> t) {
		StringBuffer sql = new StringBuffer(500);
		sql.append("select ");
		if(null!=columns&&!columns.equalsIgnoreCase("")){
			String[] cols=columns.split(",");
			for(int n=0;n<cols.length;n++){
				if(n>0){
					sql.append(",");
				}
				sql.append(cols[n]);
			}
		}else{
			sql.append(" * ");
		}
		
		sql.append(" from ").append(tablename).append(" where 1=1 ");
		String key;
		String value;
		if (conditions != null) {
			for (Map.Entry<String, Object> entry : conditions.entrySet()) {
				key = entry.getKey().toString();
				if (entry.getValue() != null && !"".equals(entry.getValue().toString())) {
					value = entry.getValue().toString();
					if (key.endsWith("-eq")) {
						sql.append(" and ").append(key.replace("-eq", "")).append(" = '").append(value).append("' ");
					} else if (key.endsWith("-like")) {
						sql.append(" and ").append(key.replace("-like", "")).append(" like '%").append(value)
								.append("%' ");
					} else if (key.endsWith("-begin")) {
						sql.append(" and ").append(key.replace("-begin", "")).append(" > '").append(value).append("' ");
					} else if (key.endsWith("-end")) {
						sql.append(" and ").append(key.replace("-end", "")).append(" < '").append(value).append("' ");
					}else if(key.endsWith("-or")){
						//或者的值，用大写OR拼成一个字符串，这边会进行分割
						String[] valus=  value.split("OR");
						String tempkey=key.replace("-or", "");
						sql.append(" and ( ");
						for(int m=0;m<valus.length;m++){
							if(m>0){
								sql.append(" or ");
							}
							if(valus[m].equalsIgnoreCase("null")){
								sql.append(tempkey).append(" is null ");
							}else{
								sql.append(tempkey).append(" = '").append(valus[m]).append("' ");
							}
						}
						sql.append(" ) "); 
					}else if(key.endsWith("-self")){
						//自定义的条件， 单条件传入
						sql.append(" and "+value);						
					}else{
						sql.append(" and ").append(key).append(" = '").append(value).append("' ");
					}
				}
			}
		}
		// 查询出所有符合条件的数量值
		int num = 0;
		String sqlcount = sql.toString().replace("*", " count(*) as num ,1 ");
		List<Object[]> lt = findBySql(sqlcount);
		if (lt != null) {
			num=Integer.parseInt(((BigInteger) lt.get(0)[0]).toString());
		}
		int s=px.size();
		// 结果集
		if(s>0){
			sql.append(" order by ");
			int jsq=0;
			for (Map.Entry<String, Object> entry : px.entrySet()) {
				if(jsq>0){
					sql.append(",");
				}
				sql.append(entry.getKey().toString()).append(" ").append(value = entry.getValue().toString()).append(" ");
				jsq++;
			}
		}
		
//		sql.append("  offset " + (page - 1) * pagesize + " rows fetch next " + pagesize + " rows only");
		sql.append("  limit " + (page - 1) * pagesize + " , " + pagesize + " ");
		List<T> list = findBySqlT(sql.toString(), t);
		if(null==list||list.size()==0){
			list=new ArrayList();
		}
		return new MyPage(list, num);
	}

	public List<Object[]> findBySql(String sql) {
		Query query = sessionFactory.getCurrentSession().createSQLQuery(sql);
		List<Object[]> list = query.list();
		return list;
	}

	/**
	 * 分页查询,可带条件、页码
	 * 
	 * @Desc 基于HQL语句查询
	 * @param sql
	 * @param conditions
	 * @param page
	 * @param pageSize
	 * @return List<Object[]>
	 */
	public List<Object[]> findListToPageByHql(final String sql, final List<String> conditions, final int page,
			final int pageSize) {
		Session session = null;
		session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		if (conditions != null && conditions.size() > 0) {
			for (int i = 0; i < conditions.size(); i++) {
				if (conditions.get(i) != null && !conditions.get(i).equals("")) {
					query.setString(i, conditions.get(i));
				}
			}
		}
		query.setFirstResult((page - 1) * pageSize);
		query.setMaxResults(pageSize);
		List list = query.list();
		return list;
	}

	public List findMapObjBySqlNoPage(final String sql) {
		List list = null;
		Session session = null;
		try {
			session = sessionFactory.getCurrentSession();
			Query query = session.createSQLQuery(sql);
			query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			list = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
		return list;
	}

	public void executeSql(String sql) {
		try {
			// jdbcTemplate.execute(sql);
			Session session = sessionFactory.getCurrentSession();
			session.createSQLQuery(sql).executeUpdate();
		} catch (RuntimeException e) {
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * 支持存储过程没有返回值
	 * 
	 * @param callSql
	 * @param inParameters
	 */
	public void executeCallNoReturn(String callSql, Object[] inParameters) {
		Session session = getSession();
		session.doWork(new Work() {
			@Override
			public void execute(Connection connection) throws SQLException {
				CallableStatement cs = connection.prepareCall(callSql);
				int inParametersLength = inParameters.length;
				for (int i = 0; i < inParametersLength; i++) {
					cs.setObject(i + 1, inParameters[i]);
				}
				cs.executeUpdate();
			}
		});
	}
}
